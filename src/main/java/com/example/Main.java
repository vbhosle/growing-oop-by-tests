package com.example;

import com.example.ui.MainWindow;
import com.example.ui.SnipersTableModel;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
    public static final String CLOSE_COMMAND_FORMAT = "SOLVersion: 1.1; Event: CLOSE;";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private final SnipersTableModel snipers = new SnipersTableModel();

    private MainWindow ui;

  @SuppressWarnings("unused")
  private List<Chat> notToBeGCd = new ArrayList<>();

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(snipers));
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);

        for(int i = 3; i < args.length; i++)
            main.joinAuction(connection, args[i]);
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws Exception {
        safelyAddItemToModel(itemId);
        Chat chat =
            connection
                .getChatManager()
                .createChat(
                    auctionId(itemId, connection), null);
        this.notToBeGCd.add(chat);

        Auction auction = new XMPPAuction(chat);
        chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(), new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers))));

        auction.join();
    }

    private void safelyAddItemToModel(final String itemId) throws Exception {
        SwingUtilities.invokeAndWait(() -> snipers.addSniper(SniperSnapshot.joining(itemId)));
    }

    private void disconnectWhenUICloses(XMPPConnection connection) {
    ui.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            System.out.println("windowClosed");
            connection.disconnect();

            //sometimes one of end to end test fails because windowClosed is not processed on MacOS.
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
          }
        });
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private class SwingThreadSniperListener implements SniperListener {

        private final SniperListener listener;

        private SwingThreadSniperListener(SniperListener listener) {
            this.listener = listener;
        }

        @Override
        public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
          SwingUtilities.invokeLater(
              new Runnable() {
                public void run() {
                    listener.sniperStateChanged(sniperSnapshot);
                }
              });
        }

    }
}

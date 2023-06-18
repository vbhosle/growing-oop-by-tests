package com.example;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import static java.lang.String.format;

public class FakeAuctionServer {
  public static final String ITEM_ID_AS_LOGIN = "auction-%s";
  public static final String AUCTION_RESOURCE = "Auction";
  public static final String XMPP_HOSTNAME = "localhost";
  private static final String AUCTION_PASSWORD = "auction";

  private final String itemId;
  private final XMPPConnection connection;
  private Chat currentChat;

  private final SimpleMessageListener simpleMessageListener = new SimpleMessageListener();

  public FakeAuctionServer(String itemId) {
    this.itemId = itemId;
    this.connection = new XMPPConnection(XMPP_HOSTNAME);
  }

  public void startSellingItem() throws XMPPException {
    connection.connect();
    connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
    connection.getChatManager().addChatListener(new ChatManagerListener() {
      @Override
      public void chatCreated(Chat chat, boolean createdLocally) {
        currentChat = chat;
        currentChat.addMessageListener(simpleMessageListener);
      }
    });
  }

  public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
    simpleMessageListener.receivesAMessage();
  }

  public void announceClosed() throws XMPPException {
    currentChat.sendMessage(new Message());
  }

  public void stop() {
    connection.disconnect();
  }

  public String getItemId() {
    return itemId;
  }
}

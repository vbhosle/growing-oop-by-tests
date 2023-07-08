package com.example;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import static com.example.Main.BID_COMMAND_FORMAT;
import static com.example.Main.CLOSE_COMMAND_FORMAT;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FakeAuctionServer {
  public static final String XMPP_HOSTNAME = "localhost";
  public static final String ITEM_ID = "item-54321";
  private static final String AUCTION_PASSWORD = "auction";

  private final String itemId;
  private final XMPPConnection connection;
  private Chat currentChat;

  private final SimpleMessageListener messageListener = new SimpleMessageListener();

  public FakeAuctionServer(String itemId) {
    this.itemId = itemId;
    this.connection = new XMPPConnection(XMPP_HOSTNAME);
  }

  public void startSellingItem() throws XMPPException {
    connection.connect();
    connection.login(format(Main.ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, Main.AUCTION_RESOURCE);
    connection.getChatManager().addChatListener(new ChatManagerListener() {
      @Override
      public void chatCreated(Chat chat, boolean createdLocally) {
        currentChat = chat;
        currentChat.addMessageListener(messageListener);
      }
    });
  }

  public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
    receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
  }

  public void announceClosed() throws XMPPException {
    currentChat.sendMessage(CLOSE_COMMAND_FORMAT);
  }

  public void stop() {
    connection.disconnect();
  }

  public String getItemId() {
    return itemId;
  }

  public void reportPrice(int price, int increment, String bidder) throws XMPPException {
    currentChat.sendMessage(format("SOLVersion: 1.1; Event: PRICE; " + "CurrentPrice: %d; Increment: %d; Bidder: %s;", price, increment, bidder));
  }

  public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
    receivesAMessageMatching(sniperId, equalTo(format(BID_COMMAND_FORMAT, bid)));
  }

  private void receivesAMessageMatching(String sniperId,
                                        Matcher<? super String> messageMatcher)throws InterruptedException {
    messageListener.receivesAMessage(messageMatcher);
    assertThat(currentChat.getParticipant(), equalTo(sniperId));
  }
}

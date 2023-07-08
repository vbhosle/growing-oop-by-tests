package com.example;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static com.example.Main.BID_COMMAND_FORMAT;
import static com.example.Main.JOIN_COMMAND_FORMAT;

public class XMPPAuction implements Auction {
  private final Chat chat;

  public XMPPAuction(Chat chat) {
    this.chat = chat;
  }

  @Override
  public void bid(int bid) {
    sendMessage(String.format(BID_COMMAND_FORMAT, bid));
  }

  @Override
  public void join() {
    sendMessage(JOIN_COMMAND_FORMAT);
  }

  private void sendMessage(final String message) {
    try {
      chat.sendMessage(message);
    }
    catch (XMPPException e) {
      e.printStackTrace();
    }
  }
}

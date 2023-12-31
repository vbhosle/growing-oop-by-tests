package com.example;

import com.example.ui.MainWindow;

public class ApplicationRunner {
  public static final String XMPP_HOSTNAME = "localhost";
  public static final String SNIPER_ID = "sniper";
  public static final String SNIPER_PASSWORD = "sniper";
  public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

  private AuctionSniperDriver driver;

  public void startBiddingIn(final FakeAuctionServer... auctions) {
    Thread thread =
        new Thread("Test Application") {
          @Override
          public void run() {
            try {
              Main.main(arguments(auctions));
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        };

    thread.setDaemon(true);
    thread.start();
    driver = new AuctionSniperDriver(1000);
    driver.hasTitle(MainWindow.APPLICATION_TITLE);
    driver.hasColumnTitles();

    for(int i = 0; i < auctions.length; i++)
      driver.showsSniperStatus(MainWindow.STATUS_JOINING); //TODO use driver.showsSniperStatus(itemId, 0, 0, MainWindow.STATUS_JOINING);
  }

  private String[] arguments(FakeAuctionServer... auctions) {
    String[] arguments = new String[auctions.length + 3];
    arguments[0] = XMPP_HOSTNAME;
    arguments[1] = SNIPER_ID;
    arguments[2] = SNIPER_PASSWORD;

    for(int i = 0; i < auctions.length; i++)
      arguments[i+3] = auctions[i].getItemId();

    return arguments;
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(MainWindow.STATUS_LOST);
  }

  public void stop() {
    if (driver != null) driver.dispose();
  }

  public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, MainWindow.STATUS_BIDDING);
  }

  public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
    driver.showsSniperStatus(
        auction.getItemId(), winningBid, winningBid, MainWindow.STATUS_WINNING);
  }

  public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, MainWindow.STATUS_WON);
  }
}

package com.example;

import com.example.ui.MainWindow;

public class ApplicationRunner {
  public static final String XMPP_HOSTNAME = "localhost";
  public static final String SNIPER_ID = "sniper";
  public static final String SNIPER_PASSWORD = "sniper";
  public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

  private AuctionSniperDriver driver;
  private String itemId;

  public void startBiddingIn(final FakeAuctionServer auction) {
    this.itemId = auction.getItemId();
    Thread thread =
        new Thread("Test Application") {
          @Override
          public void run() {
            try {
              Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
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
    driver.showsSniperStatus(MainWindow.STATUS_JOINING);
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(MainWindow.STATUS_LOST);
  }

  public void stop() {
    if (driver != null) driver.dispose();
  }

  public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
    driver.showsSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
  }

  public void hasShownSniperIsWinning(int winningBid) {
    driver.showsSniperStatus(itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
  }

  public void showsSniperHasWonAuction(int lastPrice) {
    driver.showsSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
  }
}

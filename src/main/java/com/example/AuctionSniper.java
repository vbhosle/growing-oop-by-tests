package com.example;

public class AuctionSniper implements AuctionEventListener {
  private boolean isWinning = false;
  private final Auction auction;
  private final SniperListener sniperListener;

  public AuctionSniper(Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
  }

  public void auctionClosed() {
    if(isWinning) {
      sniperListener.sniperWon();
    } else {
      sniperListener.sniperLost();
    }
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource bidder) {
    isWinning = bidder == PriceSource.FromSniper;
    if(isWinning) {
      sniperListener.sniperWinning();
    }
    else {
      auction.bid(price+increment);
      sniperListener.sniperBidding();
    }
  }
}

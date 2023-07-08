package com.example;

public class AuctionSniper implements AuctionEventListener {
  private final Auction auction;
  private final SniperListener sniperListener;

  public AuctionSniper(Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
  }

  public void auctionClosed() {
    sniperListener.sniperLost();
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource bidder) {
    if(bidder == PriceSource.FromSniper) {
      sniperListener.sniperWinning();
    } else {
      auction.bid(price+increment);
      sniperListener.sniperBidding();
    }
  }
}

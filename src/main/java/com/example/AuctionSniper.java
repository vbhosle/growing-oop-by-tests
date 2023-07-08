package com.example;

public class AuctionSniper implements AuctionEventListener {
  private boolean isWinning = false;
  private final Auction auction;
  private final SniperListener sniperListener;
  private final String itemId;
  private SniperSnapshot snapshot;

  public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
    this.itemId = itemId;
    this.auction = auction;
    this.sniperListener = sniperListener;
    this.snapshot = SniperSnapshot.joining(itemId);
  }

  public void auctionClosed() {
    if(isWinning) {
      sniperListener.sniperWon();
    } else {
      sniperListener.sniperLost();
    }
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource priceSource) {
    isWinning = priceSource == PriceSource.FromSniper;
    if(isWinning) {
      snapshot = snapshot.winning(price);
    }
    else {
      int bid = price+increment;
      auction.bid(bid);
      snapshot = snapshot.bidding(price, bid);
      sniperListener.sniperStateChanged(new SniperSnapshot(itemId, price, bid, SniperState.BIDDING));
    }
  }
}

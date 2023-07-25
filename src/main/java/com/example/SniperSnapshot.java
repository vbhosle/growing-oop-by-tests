package com.example;

import java.util.Objects;

public class SniperSnapshot {
  public final String itemId;
  public final int lastPrice;
  public final int lastBid;
  public final SniperState state;

  public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState state) {
    this.itemId = itemId;
    this.lastPrice = lastPrice;
    this.lastBid = lastBid;
    this.state = state;
  }

  public static SniperSnapshot joining(String itemId) {
    return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
  }

  public SniperSnapshot winning(int newLastPrice) {
    return new SniperSnapshot(this.itemId, newLastPrice, lastBid, SniperState.WINNING);
  }

  public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
    return new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SniperSnapshot)) return false;
    SniperSnapshot that = (SniperSnapshot) o;
    return lastPrice == that.lastPrice && lastBid == that.lastBid && Objects.equals(itemId, that.itemId) && state == that.state;
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, lastPrice, lastBid, state);
  }

  @Override
  public String toString() {
    return "SniperSnapshot{" +
            "itemId='" + itemId + '\'' +
            ", lastPrice=" + lastPrice +
            ", lastBid=" + lastBid +
            ", state=" + state +
            '}';
  }

  public SniperSnapshot closed() {
    return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
  }

  public boolean isForSameItemAs(SniperSnapshot snapshot) {
    return itemId.equals(snapshot.itemId);
  }
}

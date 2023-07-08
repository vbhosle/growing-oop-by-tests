package com.example;

import java.util.Objects;

public class SniperState {
  public final String itemId;
  public final int lastPrice;
  public final int lastBid;

  public SniperState(String itemId, int lastPrice, int lastBid) {
    this.itemId = itemId;
    this.lastPrice = lastPrice;
    this.lastBid = lastBid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SniperState)) return false;
    SniperState that = (SniperState) o;
    return Objects.equals(itemId, that.itemId) && Objects.equals(lastPrice, that.lastPrice) && Objects.equals(lastBid, that.lastBid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, lastPrice, lastBid);
  }

  @Override
  public String toString() {
    return "SniperState{" +
            "itemId='" + itemId + '\'' +
            ", lastPrice='" + lastPrice + '\'' +
            ", lastBid='" + lastBid + '\'' +
            '}';
  }
}

package com.example;

import java.util.Objects;

public class SniperState {
  private final String itemId;
  private final int lastPrice;
  private final int lastState;

  public SniperState(String itemId, int lastPrice, int lastState) {
    this.itemId = itemId;
    this.lastPrice = lastPrice;
    this.lastState = lastState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SniperState)) return false;
    SniperState that = (SniperState) o;
    return Objects.equals(itemId, that.itemId) && Objects.equals(lastPrice, that.lastPrice) && Objects.equals(lastState, that.lastState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, lastPrice, lastState);
  }

  @Override
  public String toString() {
    return "SniperState{" +
            "itemId='" + itemId + '\'' +
            ", lastPrice='" + lastPrice + '\'' +
            ", lastState='" + lastState + '\'' +
            '}';
  }
}

package com.example;

public interface SniperListener {
    void sniperLost();

    void sniperBidding();

    void sniperWinning();

    void sniperWon();

    void sniperBidding(SniperState sniperState);
}

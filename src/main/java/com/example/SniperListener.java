package com.example;

public interface SniperListener {
    void sniperLost();

    void sniperWinning();

    void sniperWon();

    void sniperStateChanged(SniperSnapshot sniperSnapshot);
}

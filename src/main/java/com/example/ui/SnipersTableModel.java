package com.example.ui;

import com.example.SniperSnapshot;

import javax.swing.table.AbstractTableModel;

import static com.example.ui.MainWindow.STATUS_JOINING;

public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0);
    private String statusText = STATUS_JOINING;
    private SniperSnapshot sniperSnapshot = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperSnapshot.itemId;
            case LAST_PRICE:
                return sniperSnapshot.lastPrice;
            case LAST_BID:
                return sniperSnapshot.lastBid;
            case SNIPER_STATE:
                return statusText;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
        fireTableRowsUpdated(0, 0);
    }

    public void sniperStatusChanged(SniperSnapshot sniperSnapshot, String statusText) {
        this.sniperSnapshot = sniperSnapshot;
        this.statusText = statusText;
        fireTableRowsUpdated(0, 0);
    }
}

package com.example.ui;

import com.example.SniperListener;
import com.example.SniperSnapshot;
import com.example.SniperState;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    private final static SniperSnapshot STARTING_UP =
      new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };

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
        return Column.at(columnIndex).valueIn(sniperSnapshot);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.sniperSnapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }
}

package com.example.ui;

import com.example.SniperSnapshot;
import com.example.SniperState;

import javax.swing.table.AbstractTableModel;

import static com.example.ui.MainWindow.STATUS_JOINING;

public class SnipersTableModel extends AbstractTableModel {
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

    static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public void sniperStatusChanged(SniperSnapshot newSnapshot) {
        this.sniperSnapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }
}

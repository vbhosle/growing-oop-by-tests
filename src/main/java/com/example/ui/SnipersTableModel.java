package com.example.ui;

import com.example.SniperListener;
import com.example.SniperSnapshot;
import com.example.SniperState;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    private static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };

    private List<SniperSnapshot> sniperSnapshots = new ArrayList<>();

    @Override
    public int getRowCount() {
        return sniperSnapshots.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(sniperSnapshots.get(rowIndex));
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        sniperSnapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    private int rowMatching(SniperSnapshot newSnapshot) {
        for (int i = 0; i < sniperSnapshots.size(); i++) {
            if (newSnapshot.isForSameItemAs(sniperSnapshots.get(i))) {
                return i;
            }
        }

        throw new RuntimeException("Cannot find match for " + newSnapshot);
    }

    public void addSniper(SniperSnapshot joining) {
        this.sniperSnapshots.add(joining);
        fireTableRowsInserted(0, 0);
    }
}

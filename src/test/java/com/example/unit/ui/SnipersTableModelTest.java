package com.example.unit.ui;

import com.example.SniperSnapshot;
import com.example.SniperState;
import com.example.ui.Column;
import com.example.ui.MainWindow;
import com.example.ui.SnipersTableModel;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static com.example.ui.SnipersTableModel.textFor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;

public class SnipersTableModelTest {
  @Rule public final JUnitRuleMockery context = new JUnitRuleMockery();

  private TableModelListener listener = context.mock(TableModelListener.class);
  private final SnipersTableModel model = new SnipersTableModel();

  @Before
  public void attachModelListener() {
    model.addTableModelListener(listener);
  }

  @Test
  public void hasEnoughColumns() {
    assertThat(model.getColumnCount(), equalTo(Column.values().length));
  }

  @Test
  public void setsSniperValuesInColumns() {
    SniperSnapshot joining = SniperSnapshot.joining("item id");
    SniperSnapshot bidding = joining.bidding(555, 666);
    context.checking( new Expectations() {{
      allowing(listener).tableChanged(with(anyInsertionEvent()));

      oneOf(listener).tableChanged(with(aChangeInRow(0)));
    }});

    model.addSniper(joining);
    model.sniperStateChanged(bidding);

    assertRowMatchesSnapshot(0, bidding);
  }

  private Matcher<TableModelEvent> aChangeInRow(int rowIndex) {
    return samePropertyValuesAs(new TableModelEvent(model, rowIndex));
  }

  private Matcher<TableModelEvent> anyInsertionEvent() {
    return hasProperty("type", equalTo(TableModelEvent.INSERT));
  }

  @Test
  public void notifiesListenersWhenAddingASniper() {
    SniperSnapshot joining = SniperSnapshot.joining("item123");
    context.checking(
        new Expectations() {
          {
            oneOf(listener).tableChanged(with(anInsertionAtRow(0)));
          }
        });

    assertEquals(0, model.getRowCount());
    model.addSniper(joining);

    assertEquals(1, model.getRowCount());
    assertRowMatchesSnapshot(0, joining);
  }

  private void assertRowMatchesSnapshot(int rowIndex, SniperSnapshot expectedSnapshot) {
    assertEquals(expectedSnapshot.itemId, model.getValueAt(rowIndex, Column.ITEM_IDENTIFIER.ordinal()));
    assertEquals(expectedSnapshot.lastPrice, model.getValueAt(rowIndex, Column.LAST_PRICE.ordinal()));
    assertEquals(expectedSnapshot.lastBid, model.getValueAt(rowIndex, Column.LAST_BID.ordinal()));
    assertEquals(textFor(expectedSnapshot.state), model.getValueAt(rowIndex, Column.SNIPER_STATE.ordinal()));
  }

  private Matcher<TableModelEvent> anInsertionAtRow(int rowIndex) {
    return samePropertyValuesAs(new TableModelEvent(model, rowIndex, rowIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
  }

  @Test public void setsUpColumnHeadings() {
    for (Column column: Column.values()) {
      assertEquals(column.name, model.getColumnName(column.ordinal()));
    }
  }

  private void assertColumnEquals(Column column, Object expected) {
    final int rowIndex = 0;
    final int columnIndex = column.ordinal();
    assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
  }

  private Matcher<TableModelEvent> aRowChangedEvent() {
    return samePropertyValuesAs(new TableModelEvent(model, 0));
  }
}

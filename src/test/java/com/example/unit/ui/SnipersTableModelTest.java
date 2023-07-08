package com.example.unit.ui;

import com.example.SniperSnapshot;
import com.example.ui.Column;
import com.example.ui.MainWindow;
import com.example.ui.SnipersTableModel;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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
    context.checking(
        new Expectations() {
          {
            oneOf(listener).tableChanged(with(aRowChangedEvent()));
          }
        });
    model.sniperStatusChanged(new SniperSnapshot("item id", 555, 666), MainWindow.STATUS_BIDDING);
    assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
    assertColumnEquals(Column.LAST_PRICE, 555);
    assertColumnEquals(Column.LAST_BID, 666);
    assertColumnEquals(Column.SNIPER_STATE, MainWindow.STATUS_BIDDING);
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

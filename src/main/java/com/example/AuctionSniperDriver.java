package com.example;

import com.example.ui.MainWindow;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.Matchers.equalTo;

public class AuctionSniperDriver extends JFrameDriver {

  public AuctionSniperDriver(int timeoutMillis) {
    super(
        new GesturePerformer(),
        JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
        new AWTEventQueueProber(timeoutMillis, 100));
  }

  public void showsSniperStatus(String statusText) {
    new JTableDriver(this).hasCell(withLabelText(equalTo(statusText)));
  }

  public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
    new JTableDriver(this)
        .hasRow(
            matching(
                withLabelText(itemId),
                withLabelText(String.valueOf(lastPrice)),
                withLabelText(String.valueOf(lastBid)),
                withLabelText(statusText))
        );
  }
}

package com.example.unit;

import com.example.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static com.example.FakeAuctionServer.ITEM_ID;
import static org.hamcrest.CoreMatchers.equalTo;

public class AuctionSniperTest {
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();
    private final Auction auction = context.mock(Auction.class);
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);
    private final States sniperState = context.states("sniper");

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        context.checking(new Expectations() {{
            oneOf(sniperListener).sniperLost();
        }});
        sniper.auctionClosed();
    }

    @Test
    public void reportsLostWhenAuctionClosesWhenBidding() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(with(any(SniperSnapshot.class)));
            then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperLost();
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void reportsWonWhenAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperWinning();
            then(sniperState.is("winning"));

            atLeast(1).of(sniperListener).sniperWon();
            when(sniperState.is("winning"));
        }});

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrivesFromOtherBidder() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;
        context.checking(new Expectations(){{
            oneOf(auction).bid(price+increment);
            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
        }});

        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations(){{
            atLeast(1).of(sniperListener).sniperWinning();
        }});

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
    }

      private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
            equalTo(state), "sniper that is ", "was") {
          @Override
          protected SniperState featureValueOf(SniperSnapshot actual) {
            return actual.state;
          }
        };
      }
}

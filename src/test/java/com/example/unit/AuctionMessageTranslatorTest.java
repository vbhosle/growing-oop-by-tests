package com.example.unit;

import com.example.AuctionEventListener;
import com.example.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static com.example.ApplicationRunner.SNIPER_XMPP_ID;
import static com.example.Main.CLOSE_COMMAND_FORMAT;

public class AuctionMessageTranslatorTest {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    public static final Chat UNUSED_CHAT = null;

    private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(SNIPER_XMPP_ID, listener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived(){
        context.checking(new Expectations() {{
            oneOf(listener).auctionClosed();
        }});

        Message message = new Message();
        message.setBody(CLOSE_COMMAND_FORMAT);

        translator.processMessage(UNUSED_CHAT, message);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        context.checking(new Expectations() {{
            exactly(1).of(listener).currentPrice(192, 7, AuctionEventListener.PriceSource.FromOtherBidder);
        }});

        Message message = new Message(); message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;" );
        translator.processMessage(UNUSED_CHAT, message);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        context.checking(new Expectations() {{
            exactly(1).of(listener).currentPrice(192, 7, AuctionEventListener.PriceSource.FromSniper);
        }});

        Message message = new Message(); message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: "+ SNIPER_XMPP_ID +";" );
        translator.processMessage(UNUSED_CHAT, message);
    }
}

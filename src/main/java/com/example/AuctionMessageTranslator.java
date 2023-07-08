package com.example;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {
  private final AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        AuctionEvent event = AuctionEvent.from(message.getBody());
        String type = event.type();
        if("CLOSE".equals(type))
            listener.auctionClosed();
        else if("PRICE".equals(type))
            listener.currentPrice(
                    Integer.parseInt(event.currentPrice()),
                    Integer.parseInt(event.increment())
            );
    }

    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<>();

        public static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for(String field: fieldsIn(messageBody))
                event.addField(field);
            return event;
        }

        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        private static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }

        public String type() {
            return fields.get("Event");
        }

        public String currentPrice() {
            return fields.get("CurrentPrice");
        }

        public String increment() {
            return fields.get("Increment");
        }
    }
}

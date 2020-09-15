package ticketing.orders.messaging.listeners;

import lombok.Value;

@Value
public class TicketUpdatedMessage {
    String id, title;
    Integer price;
    Long version;
    String orderId;
}

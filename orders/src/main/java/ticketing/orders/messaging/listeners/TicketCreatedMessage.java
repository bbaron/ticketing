package ticketing.orders.messaging.listeners;

import lombok.Value;

@Value
public class TicketCreatedMessage {
     String id, title;
     Integer price;

}

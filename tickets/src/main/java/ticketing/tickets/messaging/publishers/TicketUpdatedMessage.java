package ticketing.tickets.messaging.publishers;

import lombok.Value;

@Value
public class TicketUpdatedMessage {
    String id, title, userId;
    Integer price;
    Long version;
    String orderId;

}

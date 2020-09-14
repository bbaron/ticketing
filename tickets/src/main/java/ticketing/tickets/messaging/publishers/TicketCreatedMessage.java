package ticketing.tickets.messaging.publishers;

import lombok.Value;

@Value
public class TicketCreatedMessage {
    String id, title, userId;
    Integer price;
    Long version;

}

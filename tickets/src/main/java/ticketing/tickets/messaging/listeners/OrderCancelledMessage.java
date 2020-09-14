package ticketing.tickets.messaging.listeners;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
@Value
public class OrderCancelledMessage {
    String id;
    Ticket ticket;

    public static OrderCancelledMessage of(String id, String ticketId) {
        return new OrderCancelledMessage(id, new Ticket(ticketId));
    }


    @Value
    public static class Ticket {
        String id;

        Ticket(@JsonProperty("id") String id) {
            this.id = id;
        }
    }
}

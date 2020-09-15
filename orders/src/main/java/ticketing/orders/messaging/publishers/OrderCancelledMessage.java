package ticketing.orders.messaging.publishers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class OrderCancelledMessage {
    String id;
    Long version;
    Ticket ticket;

    public static OrderCancelledMessage of(String id, Long version, String ticketId) {
        return new OrderCancelledMessage(id, version, new Ticket(ticketId));
    }

    @Value
    public static class Ticket {
        String id;

        public Ticket(@JsonProperty("id") String id) {
            this.id = id;
        }
    }
}

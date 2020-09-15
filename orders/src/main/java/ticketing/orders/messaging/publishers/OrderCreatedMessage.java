package ticketing.orders.messaging.publishers;

import lombok.Value;
import ticketing.messaging.types.OrderStatus;

import java.time.Instant;

@Value
public class OrderCreatedMessage {
    String id, userId;
    OrderStatus status;
    Instant expiresAt;
    Long version;
    Ticket ticket;

    public static OrderCreatedMessage of(String id, String userId, OrderStatus status, Instant expiresAt, Long version, String ticketId, int price) {
        return new OrderCreatedMessage(id, userId, status, expiresAt, version, new Ticket(ticketId, price));
    }

    @Value
    public static class Ticket {
        String id;
        Integer price;
    }
}

package ticketing.orders;

import lombok.Value;
import ticketing.messaging.types.OrderStatus;

import java.time.Instant;

@Value
public class OrderResponse {
    String id;
    Long version;
    OrderStatus status;
    String userId;
    Instant expiresAt;
    TicketResponse ticket;


    public static OrderResponse of(Order order) {
        var ticket = order.getTicket();
        return new OrderResponse(order.getId(),
                order.getVersion(),
                order.getStatus(),
                order.getUserId(),
                order.getExpiration(),
                new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getPrice()));
    }

    @Value
    public static class TicketResponse {
        String id;
        String title;
        Integer price;
    }
}

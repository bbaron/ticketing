package ticketing.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketing.messaging.types.OrderStatus;
import ticketing.orders.messaging.publishers.OrderCancelledMessage;
import ticketing.orders.messaging.publishers.OrderCreatedMessage;

import java.time.Instant;

@Document
@Value
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Builder
public class Order {
    @Id
    @With
    String id;
    @With
    String userId;
    @With
    OrderStatus status;
    Instant expiration;
    @Version
    Long version;
    @DBRef
    Ticket ticket;

    public static Order of(String userId, OrderStatus status, Instant expiration, Ticket ticket) {
        return new Order(null, userId, status, expiration, null, ticket);
    }

    public OrderCreatedMessage toOrderCreatedMessage() {
        return OrderCreatedMessage.of(id, userId, status, expiration, version, ticket.getId(), ticket.getPrice());
    }
    public OrderCancelledMessage toOrderCancelledMessage() {
        return OrderCancelledMessage.of(id, version, ticket.getId());
    }
}

package ticketing.orders.events.publishers;

import ticketing.common.events.Event;
import ticketing.common.events.types.OrderStatus;

import java.util.Date;

@SuppressWarnings("unused")
public class OrderCreatedEvent implements Event {
    public String id, userId;
    public OrderStatus status;
    public Date expiresAt;
    public Long version;
    public Ticket ticket = new Ticket();

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String id, String userId,
                             Date expiresAt, Long version, OrderStatus status,
                             String ticketId, Integer ticketPrice) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.expiresAt = expiresAt;
        this.version = version;
        this.ticket = new Ticket(ticketId, ticketPrice);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{id='%s', userId='%s', status=%s, expiresAt=%s, version=%d, ticket=%s}"
                .formatted(id, userId, status, expiresAt, version, ticket);
    }

    public static class Ticket {
        public String id;
        public Integer price;

        public Ticket() {
        }

        public Ticket(String id, Integer price) {
            this.id = id;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Ticket{id='%s', price=%d}".formatted(id, price);
        }
    }
}

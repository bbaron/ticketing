package ticketing.payments.events.listeners;

import ticketing.common.events.Event;
import ticketing.common.events.types.OrderStatus;

public class OrderCreatedEvent implements Event {
    public String id, userId;
    public OrderStatus status;
    public Ticket ticket = new Ticket();

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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }


    @Override
    public String toString() {
        return "OrderCreatedEvent{id='%s', userId='%s', status=%s, ticket=%s}".formatted(id, userId, status, ticket);
    }

    public static class Ticket {
        public Integer price;

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Ticket{price=%d}".formatted(price);
        }
    }
}

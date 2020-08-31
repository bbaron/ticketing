package ticketing.tickets.events.listeners;

import ticketing.common.events.Event;

public class OrderCreatedEvent implements Event {
    public String id;
    public Ticket ticket = new Ticket();

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String id, String ticketId) {
        this.id = id;
        this.ticket = new Ticket(ticketId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{id='%s', ticket=%s}".formatted(id, ticket);
    }

    public static class Ticket {
        public String id;

        public Ticket() {
        }

        public Ticket(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Ticket{id='%s'}".formatted(id);
        }
    }
}

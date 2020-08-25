package app.events.publishers;

import ticketing.common.events.Event;

@SuppressWarnings("unused")
public class OrderCancelledEvent implements Event {
    public String id;
    public Long version;
    public Ticket ticket = new Ticket();

    public OrderCancelledEvent() {
    }

    public OrderCancelledEvent(String id, Long version, String ticketId) {
        this.id = id;
        this.version = version;
        this.ticket = new Ticket(ticketId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "OrderCancelledEvent{id='%s', version=%d, ticket=%s}".formatted(id, version, ticket);
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

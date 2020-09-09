package ticketing.tickets.messaging.listeners;

public class OrderCancelledMessage {
    public String id;
    public Ticket ticket = new Ticket();

    public OrderCancelledMessage() {
    }

    public OrderCancelledMessage(String id, String ticketId) {
        this.id = id;
        this.ticket = new Ticket(ticketId);
    }

    public OrderCancelledMessage(String id, Ticket ticket) {
        this.id = id;
        this.ticket = ticket;
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
        return "OrderCancelledEvent{id='%s', ticket=%s}".formatted(id, ticket);
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

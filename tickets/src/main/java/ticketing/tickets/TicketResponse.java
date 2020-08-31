package ticketing.tickets;

public class TicketResponse {
    private final String id;
    private final String title;
    private final Integer price;
    private final boolean reserved;

    public TicketResponse(String id, String title, Integer price, boolean reserved) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.reserved = reserved;
    }

    TicketResponse() {
        this("", "", 0, false);
    }

    public TicketResponse(Ticket ticket) {
        this(ticket.id, ticket.title, ticket.price, ticket.reserved());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isReserved() {
        return reserved;
    }

    @Override
    public String toString() {
        return "TicketResponse{id='%s', title='%s', price=%d, reserved=%s}".formatted(id, title, price, reserved);
    }
}

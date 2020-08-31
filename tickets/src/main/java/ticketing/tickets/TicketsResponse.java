package ticketing.tickets;

import java.util.ArrayList;
import java.util.List;

public class TicketsResponse {
    private final List<TicketResponse> tickets;

    public TicketsResponse(List<TicketResponse> tickets) {
        this.tickets = new ArrayList<>(tickets);
    }

    public List<TicketResponse> getTickets() {
        return tickets;
    }

    @Override
    public String toString() {
        return "TicketsResponse{tickets=%s}".formatted(tickets);
    }
}

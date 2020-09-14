package ticketing.tickets;

import lombok.Value;

@Value
public class TicketResponse {
    String id;
    String title;
    Integer price;
    boolean reserved;
}

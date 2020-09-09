package ticketing.tickets.messaging.publishers;

import lombok.Data;

@Data
public class TicketCreatedMessage {
    public String id, title, userId;
    public Integer price;
    public Long version;

    public TicketCreatedMessage() {
    }

    public TicketCreatedMessage(String id, String title, String userId, Integer price, Long version) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.price = price;
        this.version = version;
    }

}

package ticketing.tickets;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketing.tickets.messaging.publishers.TicketCreatedMessage;
import ticketing.tickets.messaging.publishers.TicketUpdatedMessage;

@Document
@Value
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
public class Ticket {
    @Id
    @With
    String id;
    @With
    String title;
    @With
    Integer price;
    String userId;
    @Version
    Long version;
    @With
    String orderId;

    public static Ticket of(String title, int price, String userId) {
        return new Ticket(null, title, price, userId, null, null);
    }

    public Ticket withoutOrderId() {
        return withOrderId(null);
    }

    public boolean reserved() {
        return orderId != null;
    }

    public TicketCreatedMessage toTicketCreatedMessage() {
        return new TicketCreatedMessage(id, title, userId, price, version);
    }

    public TicketUpdatedMessage toTicketUpdatedMessage() {
        return new TicketUpdatedMessage(id, title, userId, price, version, orderId);
    }

    public TicketResponse toTicketResponse() {
        return new TicketResponse(id, title, price, reserved());
    }
}

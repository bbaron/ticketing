package app;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static java.util.Objects.*;

@Document
public record Ticket(
        @Id
        @JsonProperty("id")
        String id,
        @JsonProperty("title")
        String title,
        @JsonProperty("price")
        Double price,
        @JsonProperty("userId")
        String userId
) {
    public Ticket() {
        this(null, null, null, null);
    }

    @PersistenceConstructor
    public Ticket(String id, String title, Double price, String userId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.userId = userId;
    }

    public Ticket(TicketRequest ticketRequest, String userId) {
        this(null,
                ticketRequest.getTitle(),
                ticketRequest.getPrice(),
                requireNonNull(userId, "userId is required"));
    }

    @SuppressWarnings("unused")
    public Ticket withId(String id) {
        // used by spring data mongo
        return new Ticket(id, title, price, userId);
    }

    public Ticket update(String title, Double price) {
        return new Ticket(id, title, price, userId);
    }
}

package app;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
@Document
public class Ticket{
        @Id
        String id;
        String title;
        Double price;
        String userId;
        @Version
        Long version;

    public Ticket() {
    }

    @PersistenceConstructor
    public Ticket(String id, String title, Double price, String userId, Long version) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.userId = userId;
        this.version = version;
    }

    public Ticket(TicketRequest ticketRequest, String userId) {
        this(null,
                ticketRequest.getTitle(),
                ticketRequest.getPrice(),
                requireNonNull(userId, "userId is required"),
                null);
    }

    @SuppressWarnings("unused")
    public Ticket withId(String id) {
        // used by spring data mongo
        return new Ticket(id, title, price, userId, version);
    }

    public Ticket update(String title, Double price) {
        return new Ticket(id, title, price, userId, version);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

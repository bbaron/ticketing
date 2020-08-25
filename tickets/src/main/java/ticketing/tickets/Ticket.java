package ticketing.tickets;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
@Document
public class Ticket {
    @Id
    public String id;
    public String title;
    public Integer price;
    public String userId;
    @Version
    public Long version;
    public String orderId;

    public Ticket() {
    }

    @PersistenceConstructor
    public Ticket(String id, String title, Integer price, String userId, Long version) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.userId = userId;
        this.version = version;
        this.orderId = null;
    }

    public Ticket(TicketRequest ticketRequest, String userId) {
        this(null,
                ticketRequest.getTitle(),
                ticketRequest.getPrice(),
                requireNonNull(userId, "userId is required"),
                null);
    }

    public Ticket(String id, String title, Integer price, String userId, Long version, String orderId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.userId = userId;
        this.version = version;
        this.orderId = orderId;
    }

    public Ticket withId(String id) {
        // used by spring data mongo
        return new Ticket(id, title, price, userId, version, orderId);
    }

    public Ticket withOrderId(String orderId) {
        return new Ticket(id, title, price, userId, version, orderId);
    }

    public Ticket update(String title, Integer price) {
        return new Ticket(id, title, price, userId, version, orderId);
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Ticket{id='%s', title='%s', price=%s, userId='%s', version=%d, orderId=%s}".formatted(id, title, price, userId, version, orderId);
    }
}

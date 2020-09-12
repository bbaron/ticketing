package ticketing.orders;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketing.messaging.types.OrderStatus;

import java.time.Instant;

@Document
public class Order {
    @Id
    public String id;
    public String userId;
    public OrderStatus status;
    public Instant expiration;
    @Version
    public Long version;
    @DBRef
    public Ticket ticket;

    public Order() {
    }

    public Order(String userId, OrderStatus status, Instant expiration, Ticket ticket) {
        this.userId = userId;
        this.status = status;
        this.expiration = expiration;
        this.ticket = ticket;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
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

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", expiration=" + expiration +
                ", version=" + version +
                ", ticket=" + ticket +
                '}';
    }
}

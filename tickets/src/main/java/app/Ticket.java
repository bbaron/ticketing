package app;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Ticket {
    @Id
    private String id;
    private String title;
    private Double price;
    private String userId;

    public Ticket() {

    }

    public Ticket(TicketRequest ticketRequest, String userId) {
        this.title = ticketRequest.getTitle();
        this.price = ticketRequest.getPrice();
        this.userId = Objects.requireNonNull(userId, "userId is required");
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

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                '}';
    }
}

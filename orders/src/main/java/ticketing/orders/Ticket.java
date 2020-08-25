package ticketing.orders;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@SuppressWarnings("unused")
public class Ticket {
    @Id
    public String id;
    public String title;
    public Integer price;
    @Version
    public Long version;

    public Ticket() {
    }

    public Ticket(String title, Integer price) {
        this.title = title;
        this.price = price;
    }

    public Ticket(String id, String title, Integer price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public Ticket(String id, String title, Integer price, Long version) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.version = version;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Ticket{id='%s', title='%s', price=%s, version=%d}".formatted(id, title, price, version);
    }
}

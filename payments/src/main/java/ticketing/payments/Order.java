package ticketing.payments;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketing.messaging.types.OrderStatus;

@Document
public class Order {
    @Id
    public String id;
    public OrderStatus status;
    public Integer price;
    public String userId;
    @Version
    public Long version;

    public Order() {
    }

    public Order(String id, OrderStatus status, Integer price, String userId) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Order{id='%s', status=%s, price=%d, userId='%s', version=%d}".formatted(id, status, price, userId, version);
    }
}

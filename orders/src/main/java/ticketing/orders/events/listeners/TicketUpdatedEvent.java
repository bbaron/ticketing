package ticketing.orders.events.listeners;

import ticketing.common.events.Event;

@SuppressWarnings("unused")
public class TicketUpdatedEvent implements Event {
    public String id, title;
    public Integer price;
    public Long version;
    public String orderId;

    public TicketUpdatedEvent() {
    }

    public TicketUpdatedEvent(String id, String title, Integer price, Long version, String orderId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.version = version;
        this.orderId = orderId;
    }

    @Override
    public String id() {
        return id;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "TicketUpdatedEvent{id=%s, title='%s', price=%s, version=%s, orderId=%s}".formatted(id, title, price, version, orderId);
    }
}

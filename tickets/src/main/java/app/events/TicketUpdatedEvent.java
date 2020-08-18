package app.events;

import ticketing.events.Event;

@SuppressWarnings("unused")
public class TicketUpdatedEvent implements Event {
    public String id, title, userId;
    public Integer price;
    public Long version;

    public TicketUpdatedEvent() {
    }

    public TicketUpdatedEvent(String id, String title, String userId, Integer price, Long version) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.price = price;
        this.version = version;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return "TicketUpdatedEvent{id='%s', title='%s', userId='%s', price=%s, version=%d}".formatted(id, title, userId, price, version);
    }

}

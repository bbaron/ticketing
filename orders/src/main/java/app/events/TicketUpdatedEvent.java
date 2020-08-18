package app.events;

import ticketing.events.Event;

public class TicketUpdatedEvent implements Event {
    public String id, title;
    public Integer price;
    public Long version;

    public TicketUpdatedEvent() {
    }

    public TicketUpdatedEvent(String id, String title, Integer price, Long version) {
        this.id = id;
        this.title = title;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TicketUpdatedEvent{id='%s', title='%s', price=%s, version=%s}".formatted(id, title, price, version);
    }
}

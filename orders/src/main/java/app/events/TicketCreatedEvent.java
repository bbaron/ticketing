package app.events;

import ticketing.events.Event;

@SuppressWarnings("unused")
public class TicketCreatedEvent implements Event {
    public String id, title;
    public Integer price;

    public TicketCreatedEvent() {
    }

    public TicketCreatedEvent(String id, String title, Integer price) {
        this.id = id;
        this.title = title;
        this.price = price;
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
        return "TicketCreatedEvent{id='%s', title='%s', price=%s}".formatted(id, title,  price);
    }
}

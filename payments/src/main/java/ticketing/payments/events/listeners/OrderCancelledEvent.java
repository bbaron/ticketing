package ticketing.payments.events.listeners;

import ticketing.common.events.Event;

public class OrderCancelledEvent implements Event {
    public String id;
    public Long version;

    public OrderCancelledEvent() {
    }

    public OrderCancelledEvent(String id, Long version) {
        this.id = id;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrderCancelledEvent{id='%s', version=%d}".formatted(id, version);
    }


}

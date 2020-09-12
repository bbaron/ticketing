package ticketing.payments.messaging.listeners;

public class OrderCancelledMessage {
    public String id;
    public Long version;

    public OrderCancelledMessage() {
    }

    public OrderCancelledMessage(String id, Long version) {
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

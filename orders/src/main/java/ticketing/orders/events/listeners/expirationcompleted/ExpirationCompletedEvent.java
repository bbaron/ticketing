package ticketing.orders.events.listeners.expirationcompleted;

import ticketing.common.events.Event;

public class ExpirationCompletedEvent implements Event {
    public String orderId;

    public ExpirationCompletedEvent() {
    }

    public ExpirationCompletedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "ExpirationCompletedEvent{orderId='%s'}".formatted(orderId);
    }
}

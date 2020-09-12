package ticketing.expiration.messaging.publishers;

public class ExpirationCompletedMessage {
    public String orderId;

    public ExpirationCompletedMessage() {
    }

    public ExpirationCompletedMessage(String orderId) {
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

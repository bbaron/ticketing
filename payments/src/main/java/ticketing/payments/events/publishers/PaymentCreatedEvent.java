package ticketing.payments.events.publishers;

import ticketing.common.events.Event;

public class PaymentCreatedEvent implements Event {
    public String id;
    public String orderId;
    public String stripeId;

    public PaymentCreatedEvent() {
    }

    public PaymentCreatedEvent(String id, String orderId, String stripeId) {
        this.id = id;
        this.orderId = orderId;
        this.stripeId = stripeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    @Override
    public String toString() {
        return "PaymentCreatedEvent{id='%s', orderId='%s', stripeId='%s'}".formatted(id, orderId, stripeId);
    }
}
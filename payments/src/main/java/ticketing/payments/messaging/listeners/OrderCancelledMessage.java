package ticketing.payments.messaging.listeners;

import lombok.Value;

@Value
public class OrderCancelledMessage {
    String id;
    Long version;

}

package ticketing.payments.messaging.publishers;

import lombok.Value;

@Value
public class PaymentCreatedMessage {
    String id;
    String orderId;
    String stripeId;
}

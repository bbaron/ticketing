package ticketing.orders.messaging.listeners;

import lombok.Value;

@Value
public class PaymentCreatedMessage {
     String id;
     String orderId;
     String stripeId;

}

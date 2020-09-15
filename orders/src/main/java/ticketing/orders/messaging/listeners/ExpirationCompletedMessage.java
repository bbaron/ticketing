package ticketing.orders.messaging.listeners;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ExpirationCompletedMessage {
    String orderId;

    public ExpirationCompletedMessage(@JsonProperty("orderId") String orderId) {
        this.orderId = orderId;
    }
}

package ticketing.payments.messaging.listeners;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import ticketing.messaging.types.OrderStatus;

@Value
public class OrderCreatedMessage {
    String id, userId;
    OrderStatus status;
    Ticket ticket;

    @Value
    public static class Ticket {
       Integer price;

       public Ticket(@JsonProperty("price") Integer price) {
           this.price = price;
       }
    }
}

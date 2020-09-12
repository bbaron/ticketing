package ticketing.orders.messaging.publishers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPublisher {
    private final OrderCreatedPublisher orderCreatedPublisher;
    private final OrderCancelledPublisher orderCancelledPublisher;

    public void publish(OrderCreatedMessage message) {
        orderCreatedPublisher.publish(message);
    }

    public void publish(OrderCancelledMessage message) {
        orderCancelledPublisher.publish(message);
    }
}

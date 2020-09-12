package ticketing.orders.messaging.publishers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ticketing.messaging.BasePublisher;

@Component
public class OrderCreatedPublisherImpl extends BasePublisher<OrderCreatedMessage> implements OrderCreatedPublisher {
    protected OrderCreatedPublisherImpl(StreamBridge streamBridge) {
        super(streamBridge, "orderCreated");
    }
}

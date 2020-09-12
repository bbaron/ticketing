package ticketing.orders.messaging.publishers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ticketing.messaging.BasePublisher;

@Component
public class OrderCancelledPublisherImpl extends BasePublisher<OrderCancelledMessage> implements OrderCancelledPublisher {
    protected OrderCancelledPublisherImpl(StreamBridge streamBridge) {
        super(streamBridge, "orderCancelled");
    }
}

package ticketing.orders.events.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

import static ticketing.common.events.Subject.*;

@Component
public class OrderCreatedPublisherImpl extends BasePublisher<OrderCreatedEvent> implements OrderCreatedPublisher {
    @Autowired
    public OrderCreatedPublisherImpl(Messenger messenger, JsonOperations jsonOperations,
                                     TicketingProperties properties) {
        super(messenger, jsonOperations, properties);
    }

    public OrderCreatedPublisherImpl(Messenger messenger, JsonOperations jsonOperations, String exchange) {
        super(messenger, jsonOperations, exchange);
    }

    @Override
    public Subject subject() {
        return OrderCreated;
    }
}

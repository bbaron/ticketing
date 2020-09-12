package ticketing.orders.events.publishers;

import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

import static ticketing.common.events.Subject.OrderCancelled;

//@Component
public class OrderCancelledPublisherImpl extends BasePublisher<OrderCancelledEvent> implements OrderCancelledPublisher {
//    @Autowired
    public OrderCancelledPublisherImpl(Messenger messenger, JsonOperations jsonOperations,
                                       TicketingProperties properties) {
        super(messenger, jsonOperations, properties);
    }

    public OrderCancelledPublisherImpl(Messenger messenger, JsonOperations jsonOperations, String exchange) {
        super(messenger, jsonOperations, exchange);
    }

    @Override
    public Subject subject() {
        return OrderCancelled;
    }
}

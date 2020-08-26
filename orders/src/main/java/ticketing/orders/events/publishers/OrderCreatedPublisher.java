package ticketing.orders.events.publishers;

import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

import static ticketing.common.events.Subject.*;

@Component
public class OrderCreatedPublisher extends BasePublisher<OrderCreatedEvent> {
    public OrderCreatedPublisher(Messenger messenger, JsonOperations jsonOperations,
                                    TicketingProperties properties) {
        super(messenger, jsonOperations, properties);
    }

    @Override
    protected Subject subject() {
        return OrderCreated;
    }
}
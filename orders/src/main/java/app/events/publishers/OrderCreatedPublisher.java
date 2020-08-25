package app.events.publishers;

import org.springframework.stereotype.Component;
import ticketing.autoconfigure.TicketingProperties;
import ticketing.events.BasePublisher;
import ticketing.events.Messenger;
import ticketing.events.Subject;
import ticketing.json.JsonOperations;

import static ticketing.events.Subject.*;

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

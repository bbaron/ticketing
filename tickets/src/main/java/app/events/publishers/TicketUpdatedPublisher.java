package app.events.publishers;

import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

import static ticketing.common.events.Subject.TicketUpdated;

@Component
public class TicketUpdatedPublisher extends BasePublisher<TicketUpdatedEvent> {
    public TicketUpdatedPublisher(Messenger messenger, JsonOperations jsonOperations, TicketingProperties properties) {
        super(messenger, jsonOperations, properties.events.exchange);
    }

    @Override
    protected Subject subject() {
        return TicketUpdated;
    }
}

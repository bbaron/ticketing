package app.events;

import org.springframework.stereotype.Component;
import ticketing.autoconfigure.TicketingProperties;
import ticketing.events.BasePublisher;
import ticketing.events.Messenger;
import ticketing.events.Subject;
import ticketing.json.JsonOperations;

import static ticketing.events.Subject.TicketUpdated;

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

package app.events;

import org.springframework.stereotype.Component;
import ticketing.autoconfigure.TicketingProperties;
import ticketing.events.BasePublisher;
import ticketing.events.Messenger;
import ticketing.events.Subject;
import ticketing.json.JsonOperations;

import static ticketing.events.Subject.TicketCreated;

@Component
public class TicketCreatedPublisher extends BasePublisher<TicketCreatedEvent> {
    public TicketCreatedPublisher(Messenger messenger, JsonOperations jsonOperations, TicketingProperties properties) {
        super(messenger, jsonOperations, properties.events.exchange);
    }

    @Override
    protected Subject subject() {
        return TicketCreated;
    }
}

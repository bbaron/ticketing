package ticketing.tickets.events.publishers;

import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

import static ticketing.common.events.Subject.TicketUpdated;

@Component
public class TicketUpdatedPublisherImpl extends BasePublisher<TicketUpdatedEvent>
        implements TicketUpdatedPublisher {
    public TicketUpdatedPublisherImpl(Messenger messenger, JsonOperations jsonOperations, TicketingProperties properties) {
        super(messenger, jsonOperations, properties.events.exchange);
    }

}

package ticketing.tickets.events.publishers;

import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.json.JsonOperations;

@Component
public class TicketCreatedPublisherImpl extends BasePublisher<TicketCreatedEvent> implements TicketCreatedPublisher {
    public TicketCreatedPublisherImpl(Messenger messenger, JsonOperations jsonOperations, TicketingProperties properties) {
        super(messenger, jsonOperations, properties);
    }

}

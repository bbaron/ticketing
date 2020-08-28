package ticketing.expiration.events.publishers;

import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

@Component
public class ExpirationCompletedPublisher extends BasePublisher<ExpirationCompletedEvent> {
    protected ExpirationCompletedPublisher(Messenger messenger, JsonOperations jsonOperations, TicketingProperties properties) {
        super(messenger, jsonOperations, properties);
    }

    @Override
    public Subject subject() {
        return Subject.ExpirationCompleted;
    }
}

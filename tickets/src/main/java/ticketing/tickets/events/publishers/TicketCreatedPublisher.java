package ticketing.tickets.events.publishers;

import ticketing.common.events.Publisher;
import ticketing.common.events.Subject;

@Deprecated
public interface TicketCreatedPublisher extends Publisher<TicketCreatedEvent> {
    @Override
    default Subject subject() {
        return Subject.TicketCreated;
    }
}

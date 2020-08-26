package ticketing.tickets.events.publishers;

import ticketing.common.events.Publisher;
import ticketing.common.events.Subject;

public interface TicketCreatedPublisher extends Publisher<TicketCreatedEvent> {
    @Override
    default Subject subject() {
        return Subject.TicketCreated;
    }
}

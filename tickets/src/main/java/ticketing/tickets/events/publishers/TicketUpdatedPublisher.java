package ticketing.tickets.events.publishers;

import ticketing.common.events.Publisher;
import ticketing.common.events.Subject;

import static ticketing.common.events.Subject.*;

@Deprecated
public interface TicketUpdatedPublisher extends Publisher<TicketUpdatedEvent> {
    @Override
    default Subject subject() {
        return TicketUpdated;
    }
}

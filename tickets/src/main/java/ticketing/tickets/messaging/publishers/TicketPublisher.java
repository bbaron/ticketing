package ticketing.tickets.messaging.publishers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketPublisher {
    private final TicketCreatedPublisher ticketCreatedPublisher;
    private final TicketUpdatedPublisher ticketUpdatedPublisher;

    public void publish(TicketCreatedMessage event) {
        ticketCreatedPublisher.publish(event);
    }

    public void publish(TicketUpdatedMessage event) {
        ticketUpdatedPublisher.publish(event);
    }
}

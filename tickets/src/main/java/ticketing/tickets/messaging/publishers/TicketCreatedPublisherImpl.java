package ticketing.tickets.messaging.publishers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ticketing.messaging.BasePublisher;

@Component
public class TicketCreatedPublisherImpl extends BasePublisher<TicketCreatedMessage> implements TicketCreatedPublisher {
    protected TicketCreatedPublisherImpl(StreamBridge streamBridge) {
        super(streamBridge, "ticketCreated");
    }
}

package ticketing.tickets.messaging.publishers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ticketing.messaging.BasePublisher;

@Component
public class TicketUpdatedPublisherImpl extends BasePublisher<TicketUpdatedMessage> implements TicketUpdatedPublisher {
    protected TicketUpdatedPublisherImpl(StreamBridge streamBridge) {
        super(streamBridge, "ticketUpdated");
    }
}

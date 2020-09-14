package ticketing.tickets.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ticketing.common.mongodb.AbstractOnInsertOrUpdateMongoEventListener;
import ticketing.common.mongodb.AfterInsertEvent;
import ticketing.common.mongodb.AfterUpdateEvent;
import ticketing.common.mongodb.MongoTicketingListener;
import ticketing.tickets.Ticket;
import ticketing.tickets.messaging.publishers.TicketCreatedMessage;
import ticketing.tickets.messaging.publishers.TicketPublisher;
import ticketing.tickets.messaging.publishers.TicketUpdatedMessage;

@Component
@RequiredArgsConstructor
public class MongoTicketListener extends AbstractOnInsertOrUpdateMongoEventListener<Ticket> implements MongoTicketingListener<Ticket> {
    private final TicketPublisher ticketPublisher;

    @Override
    public void onAfterInsert(AfterInsertEvent<Ticket> afterInsertEvent) {
        super.onAfterInsert(afterInsertEvent);
        Ticket ticket = afterInsertEvent.getSource();
        ticketPublisher.publish(ticket.toTicketCreatedMessage());
    }

    @Override
    public void onAfterUpdate(AfterUpdateEvent<Ticket> afterUpdateEvent) {
        super.onAfterUpdate(afterUpdateEvent);
        Ticket ticket = afterUpdateEvent.getSource();
        ticketPublisher.publish(ticket.toTicketUpdatedMessage());
    }
}

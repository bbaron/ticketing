package ticketing.tickets.events;

import org.springframework.stereotype.Component;
import ticketing.common.mongodb.AbstractOnInsertOrUpdateMongoEventListener;
import ticketing.common.mongodb.AfterInsertEvent;
import ticketing.common.mongodb.AfterUpdateEvent;
import ticketing.common.mongodb.MongoTicketingListener;
import ticketing.tickets.Ticket;
import ticketing.tickets.events.publishers.TicketCreatedEvent;
import ticketing.tickets.events.publishers.TicketCreatedPublisher;
import ticketing.tickets.events.publishers.TicketUpdatedEvent;
import ticketing.tickets.events.publishers.TicketUpdatedPublisher;

//@Component
@Deprecated
public class MongoTicketListener extends AbstractOnInsertOrUpdateMongoEventListener<Ticket> implements MongoTicketingListener<Ticket> {
    private final TicketCreatedPublisher ticketCreatedPublisher;
    private final TicketUpdatedPublisher ticketUpdatedPublisher;

    public MongoTicketListener(TicketCreatedPublisher ticketCreatedPublisher, TicketUpdatedPublisher ticketUpdatedPublisher) {
        this.ticketCreatedPublisher = ticketCreatedPublisher;
        this.ticketUpdatedPublisher = ticketUpdatedPublisher;
    }

    @Override
    public void onAfterInsert(AfterInsertEvent<Ticket> afterInsertEvent) {
        super.onAfterInsert(afterInsertEvent);
        Ticket ticket = afterInsertEvent.getSource();
        var event = new TicketCreatedEvent(ticket.id, ticket.title, ticket.userId, ticket.price, ticket.version);
        ticketCreatedPublisher.publish(event);
    }

    @Override
    public void onAfterUpdate(AfterUpdateEvent<Ticket> afterUpdateEvent) {
        super.onAfterUpdate(afterUpdateEvent);
        Ticket ticket = afterUpdateEvent.getSource();
        var event = new TicketUpdatedEvent(ticket.id, ticket.title, ticket.userId, ticket.price, ticket.version, ticket.orderId);
        ticketUpdatedPublisher.publish(event);
    }
}

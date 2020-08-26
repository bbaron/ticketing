package ticketing.tickets.events;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;
import ticketing.tickets.TicketRequest;
import ticketing.tickets.events.publishers.TicketCreatedEvent;
import ticketing.tickets.events.publishers.TicketCreatedPublisher;
import ticketing.tickets.events.publishers.TicketUpdatedEvent;
import ticketing.tickets.events.publishers.TicketUpdatedPublisher;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MongoTicketListenerTests {
    @Autowired
    TicketRepository repository;
    @MockBean
    TicketCreatedPublisher ticketCreatedPublisher;
    @MockBean
    TicketUpdatedPublisher ticketUpdatedPublisher;

    final TicketRequest request = new TicketRequest("concert", 20);
    final String user = "user";

    @Test
    void when_ticket_is_created_ticket_created_event_is_emitted() {
        var ticket = new Ticket(request, user);
        repository.insert(ticket);
        verify(ticketCreatedPublisher).publish(any(TicketCreatedEvent.class));
    }

    @Test
    void when_ticket_is_updated_ticket_updated_event_is_emitted() {
        var ticket = new Ticket(request, user);
        ticket = repository.insert(ticket);
        ticket = repository.findById(ticket.id).orElseThrow();
        ticket.setOrderId("asdf");
        repository.save(ticket);
        verify(ticketUpdatedPublisher).publish(any(TicketUpdatedEvent.class));
    }
}

package ticketing.tickets.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;
import ticketing.tickets.TicketRequest;
import ticketing.tickets.messaging.publishers.TicketCreatedMessage;
import ticketing.tickets.messaging.publishers.TicketCreatedPublisher;
import ticketing.tickets.messaging.publishers.TicketUpdatedMessage;
import ticketing.tickets.messaging.publishers.TicketUpdatedPublisher;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MongoTicketListenerTests {
    final TicketRepository repository;
    @MockBean
    TicketCreatedPublisher ticketCreatedPublisher;
    @MockBean
    TicketUpdatedPublisher ticketUpdatedPublisher;

    final TicketRequest request = new TicketRequest("concert", 20);
    final String user = "user";

    @Autowired
    MongoTicketListenerTests(TicketRepository repository) {
        this.repository = repository;
    }

    @Test
    void when_ticket_is_created_ticket_created_event_is_emitted() {
        var ticket = Ticket.of(request.getTitle(), request.getPrice(), user);
        repository.insert(ticket);
        verify(ticketCreatedPublisher).publish(any(TicketCreatedMessage.class));
    }

    @Test
    void when_ticket_is_updated_ticket_updated_event_is_emitted() {
        var ticket = Ticket.of(request.getTitle(), request.getPrice(), user);
        ticket = repository.insert(ticket);
        assertNotNull(ticket.getId());
        ticket = repository.findById(ticket.getId())
                           .orElseThrow().withOrderId("asdf");
        repository.save(ticket);
        verify(ticketUpdatedPublisher).publish(any(TicketUpdatedMessage.class));
    }

}

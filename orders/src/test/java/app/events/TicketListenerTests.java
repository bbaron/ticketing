package app.events;

import app.Ticket;
import app.TicketRepository;
import app.events.listeners.TicketCreatedEvent;
import app.events.listeners.TicketCreatedListener;
import app.events.listeners.TicketUpdatedEvent;
import app.events.listeners.TicketUpdatedListener;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ticketing.json.JsonOperations;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

class TicketListenerTests {

    private TicketCreatedListener ticketCreatedListener;
    private TicketUpdatedListener ticketUpdatedListener;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private JsonOperations jsonOperations;

    @BeforeEach
    void setUp() {
        initMocks(this);
        ticketCreatedListener = new TicketCreatedListener(jsonOperations, ticketRepository);
        ticketUpdatedListener = new TicketUpdatedListener(jsonOperations, ticketRepository);
    }

    @Test
    void creates_and_saves_a_ticket() {
        String id = ObjectId.get().toHexString();
        var title = "asdf";
        var price = 20;
        var event = new TicketCreatedEvent(id, title, price);
        var message = """
                {"id":"%s","title":"%s","userId","user","price":%s,"version":0}"""
                .formatted(id, title, price);
        when(jsonOperations.readValue(message, TicketCreatedEvent.class)).thenReturn(event);
        ticketCreatedListener.receiveMessage(message);
        verify(ticketRepository).save(argThat(allOf(
                hasProperty("id", equalTo(id)),
                hasProperty("title", equalTo(title)),
                hasProperty("price", equalTo(price)))));

    }

    @Test
    void updates_a_ticket() {
        String id = ObjectId.get().toHexString();
        var current = new Ticket(id, "asdf", 20, 0L);
        var next = new Ticket(id, "qwerty", 30, 1L);
        when(ticketRepository.findDistinctByIdAndVersion(id, current.version))
                .thenReturn(Optional.of(new Ticket(id, current.title, current.price, current.version)));
        String message = """
                {"id":"%s","title":"%s","userId":"user","price":%s,"version":%s}"""
                .formatted(id, next.title, next.price, next.version);
        var event = new TicketUpdatedEvent(id, next.title, next.price, next.version);
        when(jsonOperations.readValue(message, TicketUpdatedEvent.class)).thenReturn(event);
        ticketUpdatedListener.receiveMessage(message);
        verify(ticketRepository).save(argThat(allOf(
                hasProperty("id", equalTo(id)),
                hasProperty("title", equalTo(next.title)),
                hasProperty("price", equalTo(next.price)))));

    }
}

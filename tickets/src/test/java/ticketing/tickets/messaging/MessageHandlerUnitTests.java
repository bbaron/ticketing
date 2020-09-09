package ticketing.tickets.messaging;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;
import ticketing.tickets.messaging.listeners.OrderCreatedMessage;
import ticketing.tickets.messaging.listeners.OrderHandlers;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class MessageHandlerUnitTests {
    private OrderHandlers messageHandlers;
    @Mock
    private TicketRepository ticketRepository;
    private final String ticketId = ObjectId.get().toHexString();
    private final String orderId = ObjectId.get().toHexString();

    @BeforeEach
    void setUp() {
        initMocks(this);
        messageHandlers = new OrderHandlers(ticketRepository);
    }

    @Test
    void sets_the_order_id_of_the_ticket() {
        var ticket = new Ticket(ticketId, "concert", 20, "user", null);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        var message = new OrderCreatedMessage(orderId, ticketId);
        messageHandlers.handleOrderCreated(message);
        verify(ticketRepository).save(argThat(hasProperty("orderId", equalTo(orderId))));
    }
}

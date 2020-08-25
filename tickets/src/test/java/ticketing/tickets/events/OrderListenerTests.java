package ticketing.tickets.events;

import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;
import ticketing.tickets.events.listeners.OrderCreatedEvent;
import ticketing.tickets.events.listeners.OrderCreatedListener;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ticketing.common.json.JsonOperations;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class OrderListenerTests {
    private OrderCreatedListener orderCreatedListener;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private JsonOperations jsonOperations;
    private final String ticketId = ObjectId.get().toHexString();
    private final String orderId = ObjectId.get().toHexString();

    @BeforeEach
    void setUp() {
        initMocks(this);
        orderCreatedListener = new OrderCreatedListener(jsonOperations, ticketRepository);
    }

    @Test
    void sets_the_order_id_of_the_ticket() {
        var ticket = new Ticket(ticketId, "concert", 20, "user", null);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        var message = """
                {"id":"%s",
                "userId": "user",
                "status": "Created",
                "expiresAt": null,
                "version": 0,
                "ticket" : {"id: "%s", "price": %d}
                """.formatted(orderId, ticket.id, ticket.price);
        var event = new  OrderCreatedEvent(orderId, ticketId);
        when(jsonOperations.readValue(message, OrderCreatedEvent.class))
                .thenReturn(event);
        orderCreatedListener.receiveMessage(message);
        verify(ticketRepository).save(argThat(hasProperty("orderId", equalTo(orderId))));
    }
}

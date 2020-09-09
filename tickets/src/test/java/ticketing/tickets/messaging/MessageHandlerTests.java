package ticketing.tickets.messaging;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;
import ticketing.tickets.TicketRequest;
import ticketing.tickets.messaging.listeners.OrderCancelledMessage;
import ticketing.tickets.messaging.listeners.OrderCreatedMessage;
import ticketing.tickets.messaging.listeners.OrderHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@MockBean(AbstractMongoEventListener.class)
public class MessageHandlerTests {
    final OrderHandlers orderHandlers;
    final TicketRepository ticketRepository;
    private final String orderId = ObjectId.get()
                                           .toHexString();
    private final String userId = ObjectId.get()
                                          .toHexString();

    @Autowired
    public MessageHandlerTests(OrderHandlers orderHandlers, TicketRepository ticketRepository) {
        this.orderHandlers = orderHandlers;
        this.ticketRepository = ticketRepository;
    }

    @Test
    void created_listener_sets_the_order_id_of_the_ticket() {
        var ticket = new Ticket(new TicketRequest("concert", 20), userId);
        ticket = ticketRepository.save(ticket);
        OrderCreatedMessage message = new OrderCreatedMessage(orderId, ticket.id);
        orderHandlers.handleOrderCreated(message);
        ticket = ticketRepository.findById(ticket.id)
                                 .orElseThrow();
        assertEquals(orderId, ticket.orderId);
    }

    @Test
    void cancelled_listener_nulls_the_order_id_of_the_ticket() {
        var ticket = new Ticket(new TicketRequest("concert", 20), userId);
        ticket.setOrderId(orderId);
        ticket = ticketRepository.save(ticket);
        var message = new OrderCancelledMessage(orderId, ticket.id);
        orderHandlers.handleOrderCancelled(message);
        ticket = ticketRepository.findById(ticket.id)
                                 .orElseThrow();
        assertNull(ticket.orderId);

    }
}

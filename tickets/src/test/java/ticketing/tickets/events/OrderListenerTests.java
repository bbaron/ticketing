package ticketing.tickets.events;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;
import ticketing.tickets.TicketRequest;
import ticketing.tickets.events.listeners.OrderCancelledListener;
import ticketing.tickets.events.listeners.OrderCreatedListener;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@MockBean(AbstractMongoEventListener.class)
@Disabled
public class OrderListenerTests {
    @Autowired
    OrderCreatedListener orderCreatedListener;
    @Autowired
    OrderCancelledListener orderCancelledListener;
    @Autowired
    TicketRepository ticketRepository;
    private final String orderId = ObjectId.get().toHexString();
    private final String userId = ObjectId.get().toHexString();

    @Test
    void created_listener_sets_the_order_id_of_the_ticket() {
        var ticket = new Ticket(new TicketRequest("concert", 20), userId);
        ticket = ticketRepository.save(ticket);
        var message = """
                {"id":"%s",
                "userId": "user",
                "status": "Created",
                "expiresAt": null,
                "version": 0,
                "ticket" : {"id": "%s", "price": %d}}
                """.formatted(orderId, ticket.id, ticket.price);
        orderCreatedListener.receiveMessage(message);
        ticket = ticketRepository.findById(ticket.id).orElseThrow();
        assertEquals(orderId, ticket.orderId);
    }

    @Test
    void cancelled_listener_nulls_the_order_id_of_the_ticket() {
        var ticket = new Ticket(new TicketRequest("concert", 20), userId);
        ticket.setOrderId(orderId);
        ticket = ticketRepository.save(ticket);
        var message = """
                {"id":"%s",
                "ticket" : {"id": "%s"}}
                """.formatted(orderId, ticket.id);
        ticket = ticketRepository.findById(ticket.id).orElseThrow();
        assertNotNull(ticket.orderId);
        orderCancelledListener.receiveMessage(message);
        ticket = ticketRepository.findById(ticket.id).orElseThrow();
        assertNull(ticket.orderId);
    }
}

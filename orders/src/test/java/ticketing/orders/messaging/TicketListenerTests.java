package ticketing.orders.messaging;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.messaging.support.GenericMessage;
import ticketing.messaging.test.MessageIO;
import ticketing.messaging.test.TestMessagingConfiguration;
import ticketing.messaging.types.OrderStatus;
import ticketing.orders.Order;
import ticketing.orders.OrderRepository;
import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ticketing.messaging.types.OrderStatus.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Import(TestMessagingConfiguration.class)
public class TicketListenerTests {
    final MessageIO messageIO;
    final TicketRepository ticketRepository;
    final OrderRepository orderRepository;

    @Autowired
    public TicketListenerTests(MessageIO messageIO, TicketRepository ticketRepository, OrderRepository orderRepository) {
        this.messageIO = messageIO;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
    }

    @Test
    void creates_and_saves_a_new_ticket() {
        String id = ObjectId.get().toHexString();
        var title = "asdf";
        var price = 20;
        var expected = new Ticket(id, title, price, 0L);
        var payload = """
                {"id":"%s",
                "title":"%s",
                "userId":"user",
                "price":%s,
                "version":0}"""
                .formatted(id, title, price);
        var message = new GenericMessage<>(payload);
        messageIO.input.send(message, "ticketCreated");
        assertTrue(ticketRepository.findOne(Example.of(expected)).isPresent(),
                "ticket was not saved by ticketCreated handler");
    }

    @Test
    void updates_and_saves_an_updated_ticket() {
        var title = "asdf";
        var price = 20;
        var ticket = new Ticket(null, title, price);
        ticketRepository.insert(ticket);
        var expected = new Ticket(ticket.id, ticket.title.toUpperCase(),
                ticket.price + 10, ticket.version + 1);
        var payload = """
                {"id":"%s",
                "title":"%s",
                "userId":"user",
                "price":%s,
                "version":%s}"""
                .formatted(expected.id, expected.title, expected.price, expected.version);
        var message = new GenericMessage<>(payload);
        messageIO.input.send(message, "ticketUpdated");
        assertTrue(ticketRepository.findOne(Example.of(expected)).isPresent(),
                "ticket was not saved by ticketUpdated handler");
    }

    @Test
    void completed_order_is_not_cancelled() {
        orderChangedTest(Completed, Completed, "expirationCompleted");
    }

    @Test
    void non_completed_order_is_cancelled() {
        orderChangedTest(Created, Cancelled,"expirationCompleted");
    }

    @Test
    void paid_order_is_completed() {
        orderChangedTest(Created, Completed,"paymentCreated");
    }

    private void orderChangedTest(OrderStatus beforeStatus, OrderStatus afterStatus, String bindingName) {
        var ticket = new Ticket(null, "title", 10);
        ticketRepository.insert(ticket);
        Order order = new Order("user", beforeStatus, Instant.now(), ticket);
        orderRepository.insert(order);
        var payload = """
                {"orderId": "%s"}""".formatted(order.id);
        var message = new GenericMessage<>(payload);
        messageIO.input.send(message, bindingName);
        order = orderRepository.findById(order.id).orElseThrow();
        assertEquals(afterStatus, order.status);

    }
}

package ticketing.orders.events;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.lang.Nullable;
import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;
import ticketing.orders.events.listeners.ticketcreated.TicketCreatedListener;
import ticketing.orders.events.listeners.ticketupdated.TicketUpdatedListener;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Disabled
class TicketListenerTests {
    @Autowired
    TicketCreatedListener ticketCreatedListener;
    @Autowired
    TicketUpdatedListener ticketUpdatedListener;
    @Autowired
    TicketRepository ticketRepository;

    @Test
    void creates_and_saves_a_ticket() {
        String id = ObjectId.get().toHexString();
        var title = "asdf";
        var price = 20;
        var expected = new Ticket(id, title, price, 0L);
        var message = """
                {"id":"%s",
                "title":"%s",
                "userId":"user",
                "price":%s,
                "version":0}"""
                .formatted(id, title, price);
        ticketCreatedListener.receiveMessage(message);
        ticketRepository.findOne(Example.of(expected)).ifPresentOrElse(actual ->
                assertAll(
                        () -> assertEquals(expected.id, actual.id, "id mis-match"),
                        () -> assertEquals(expected.title, actual.title, "title mis-match"),
                        () -> assertEquals(expected.version, actual.version, "title mis-match"),
                        () -> assertEquals(expected.price, actual.price, "price mis-match"),
                        () -> assertNull(actual.orderId, "orderId should be null")
                ), () -> fail("ticket was not saved in event listener"));
    }

    private void testAfterInsertion(BiConsumer<String, Ticket> afterInsertion, @Nullable Function<Ticket, Ticket> modificator) {

        String id = ObjectId.get().toHexString();
        String orderId = ObjectId.get().toHexString();
        var current = new Ticket(id, "asdf", 20);
        current = ticketRepository.insert(current);
        var ticket = new Ticket(id, "qwerty", 30, current.version + 1);
        ticket.setOrderId(orderId);
        if (modificator != null) {
            ticket = modificator.apply(ticket);
        }
        String message = """
                {"id":"%s",
                "title":"%s",
                "userId":"user",
                "price":%d,
                "version":%d,
                "orderId":"%s"}"""
                .formatted(id, ticket.title, ticket.price, ticket.version, ticket.orderId);
        afterInsertion.accept(message, ticket);
    }

    @Test
    void updates_a_ticket() {

        testAfterInsertion((message, ticket) -> {
            ticketUpdatedListener.receiveMessage(message);
            Ticket actual = ticketRepository.findById(ticket.id).orElseThrow();
            assertAll(
                    () -> assertEquals(ticket.id, actual.id, "id mis-match"),
                    () -> assertEquals(ticket.title, actual.title, "title mis-match"),
                    () -> assertEquals(ticket.version, actual.version, "title mis-match"),
                    () -> assertEquals(ticket.price, actual.price, "price mis-match"),
                    () -> assertEquals(ticket.orderId, actual.orderId, "orderId mis-match")
            );

        }, null);
    }

    @Test
    void exception_on_out_of_sequence_event() {
        testAfterInsertion((message, ticket) ->
                        assertThrows(IllegalStateException.class,
                                () -> ticketUpdatedListener.receiveMessage(message)),
                ticket -> {
                    ticket.version++;
                    return ticket;
                });
    }
}

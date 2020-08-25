package ticketing.tickets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TicketVersionTests {
    @Autowired
    TicketRepository repository;

    @Test
    @DisplayName("implements optimistic concurrency control")
    void test1() {
        var ticket = new Ticket(null, "concert", 5, "asdf", null);
        ticket = repository.insert(ticket);
        var t1 = repository.findById(ticket.getId()).orElseThrow();
        var t2 = repository.findById(ticket.getId()).orElseThrow();
        t1.setPrice(10);
        t2.setPrice(15);
        repository.save(t1);
        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(t2));
    }

    @Test
    @DisplayName("increments version on multiple saves")
    void test2() {
        var ticket = new Ticket(null, "concert", 5, "asdf", null);
        ticket = repository.insert(ticket);
        var v = ticket.version;
        assertEquals(v, ticket.version);
        repository.save(ticket);
        assertEquals(++v, ticket.version);
        repository.save(ticket);
        assertEquals(++v, ticket.version);
    }
}

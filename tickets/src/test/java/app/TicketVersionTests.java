package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketVersionTests {
    @Autowired
    TicketRepository repository;

    @Test
    @DisplayName("implements optimistic concurrency control")
    void test1() {
        var ticket = new Ticket(null, "concert", 5.0, "asdf", null);
        ticket = repository.insert(ticket);
        var t1 = repository.findById(ticket.getId()).orElseThrow();
        var t2 = repository.findById(ticket.getId()).orElseThrow();
        t1.setPrice(10.0);
        t2.setPrice(15.0);
        repository.save(t1);
        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(t2));
    }

    @Test
    @DisplayName("increments version on multiple saves")
    void test2() {
        var ticket = new Ticket(null, "concert", 5.0, "asdf", null);
        ticket = repository.insert(ticket);
        var v = ticket.version;
        assertEquals(v, ticket.version);
        repository.save(ticket);
        assertEquals(++v, ticket.version);
        repository.save(ticket);
        assertEquals(++v, ticket.version);
    }
}

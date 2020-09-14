package ticketing.tickets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@MockBean(AbstractMongoEventListener.class)
class TicketVersionTests {
    @Autowired
    TicketRepository repository;

    @Test
    @DisplayName("implements optimistic concurrency control")
    void test1() {
        var ticket = new Ticket(null, "concert", 5, "asdf", null, null);
        ticket = repository.insert(ticket);
        var t1 = repository.findById(ticket.getId())
                           .orElseThrow()
                           .withPrice(10);
        var t2 = repository.findById(ticket.getId())
                           .orElseThrow()
                           .withPrice(15);
        repository.save(t1);
        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(t2));
    }

    @Test
    @DisplayName("increments version on multiple saves")
    void test2() {
        var ticket = new Ticket(null, "concert", 5, "asdf", null, null);
        ticket = repository.insert(ticket);
        long v = requireNonNull(ticket.getVersion());
        assertEquals(v, ticket.getVersion());
        var t2 = repository.save(ticket);
        assertEquals(++v, t2.getVersion());
        var t3 = repository.save(t2);
        assertEquals(++v, t3.getVersion());
    }
}

package ticketing.tickets;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataMongoTest
class TicketRepositoryTests {
    final TicketRepository ticketRepository;

    @Autowired
    TicketRepositoryTests(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Test
    void insert() {
        var user = ObjectId.get()
                           .toHexString();
        var ticket = Ticket.of("title", 20, user);
        var saved = ticketRepository.insert(ticket);
        assertNotNull(saved.getId());
    }

    @Test
    void update() {
        var user = ObjectId.get()
                           .toHexString();
        var t1 = Ticket.of("title", 20, user);
        var t2 = ticketRepository.insert(t1);
        assertNotNull(t2.getId());
        var t3 = ticketRepository.findById(t2.getId())
                                 .orElseThrow();
        assertNull(t3.getOrderId());
        var t4 = t3.withOrderId(ObjectId.get()
                                        .toHexString());
        assertNotNull(t4.getOrderId());
        var t5 = ticketRepository.save(t4);
        assertNotNull(t5.getId());
        var t6 = ticketRepository.findById(t5.getId())
                                 .orElseThrow();
        assertNotNull(t6.getOrderId());
    }
}

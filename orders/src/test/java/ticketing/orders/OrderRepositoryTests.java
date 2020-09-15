package ticketing.orders;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ticketing.messaging.types.OrderStatus;

import java.time.Instant;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static ticketing.messaging.types.OrderStatus.Cancelled;
import static ticketing.messaging.types.OrderStatus.Created;

@DataMongoTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OrderRepositoryTests {
    final OrderRepository orderRepository;
    final TicketRepository ticketRepository;
    final String userId = ObjectId.get()
                                  .toHexString();
    final OrderStatus status = Created;
    final Instant exp = Instant.now();
    final String ticketId = ObjectId.get()
                                    .toHexString();
    final String title = "concert";
    final int price = 20;

    @Test
    void insert() {
        Ticket ticket = ticketRepository.insert(Ticket.of(ticketId, title, price));
        var order = orderRepository.insert(Order.of(userId, status, exp, ticket));
        assertThat(order.getId()).isNotNull();
    }

    @Test
    void update() {
        Ticket ticket = ticketRepository.insert(Ticket.of(ticketId, title, price));
        var orderId = orderRepository.insert(Order.of(userId, status, exp, ticket))
                                     .getId();
        assertThat(orderId).isNotNull();
        var order = orderRepository.findById(orderId)
                                   .orElseThrow();
        orderRepository.save(order.withStatus(Cancelled));
        order = orderRepository.findById(orderId)
                               .orElseThrow();
        assertThat(order.getStatus()).isEqualTo(Cancelled);
    }

    @Test
    void find_by_ticket() {
        Ticket ticket = ticketRepository.insert(Ticket.of(ticketId, title, price));
        orderRepository.insert(Order.of(userId, status, exp, ticket));
        orderRepository.insert(Order.of(userId, Cancelled, exp, ticket));
        range(0, 5)
                .boxed()
                .map(i -> ticket.withId(ObjectId.get()
                                                .toHexString()))
                .map(ticketRepository::insert)
                .map(t -> Order.of(userId, status, exp, t))
                .forEach(orderRepository::insert);
        var orders = orderRepository.findByTicket(ticket);
        assertThat(orders).hasSize(2);
    }

    @Test
    void find_by_user() {
        Ticket ticket = ticketRepository.insert(Ticket.of(ticketId, title, price));
        String theUser = ObjectId.get()
                                 .toHexString();
        orderRepository.insert(Order.of(theUser, status, exp, ticket));
        orderRepository.insert(Order.of(theUser, Cancelled, exp, ticket));
        range(0, 5)
                .boxed()
                .map(i -> Order.of(ObjectId.get()
                                           .toHexString(), status, exp, ticket))
                .forEach(orderRepository::insert);
        var orders = orderRepository.findByUserId(theUser);
        assertThat(orders).hasSize(2);
    }
}

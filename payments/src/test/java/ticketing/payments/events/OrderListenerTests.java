package ticketing.payments.events;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import ticketing.common.events.types.OrderStatus;
import ticketing.payments.Order;
import ticketing.payments.OrderRepository;
import ticketing.payments.events.listeners.OrderCancelledListener;
import ticketing.payments.events.listeners.OrderCreatedListener;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.fail;
import static ticketing.common.events.types.OrderStatus.Cancelled;
import static ticketing.common.events.types.OrderStatus.Created;

@SpringBootTest
class OrderListenerTests {
    final OrderCreatedListener orderCreatedListener;
    final OrderCancelledListener orderCancelledListener;
    final OrderRepository orderRepository;

    @Autowired
    OrderListenerTests(OrderCreatedListener orderCreatedListener, OrderCancelledListener orderCancelledListener, OrderRepository orderRepository) {
        this.orderCreatedListener = orderCreatedListener;
        this.orderCancelledListener = orderCancelledListener;
        this.orderRepository = orderRepository;
    }

    @Test
    void creates_and_saves_a_ticket() {
        String orderId = ObjectId.get().toHexString();
        String ticketId = ObjectId.get().toHexString();
        Instant expiresAt = Instant.now();
        OrderStatus status = Created;
        Integer price = 20;
        String userId = ObjectId.get().toHexString();
        Long version = 0L;

        var expected = new Order(orderId, status, price, userId);
        expected.version = version;
        var message = """
                {"id":"%s",
                "status":"%s",
                "userId":"%s",
                "expiresAt":"%s",
                "version":%s,
                "ticket":{"id":"%s", "price":%s}}"""
                .formatted(orderId, status, userId, expiresAt, version, ticketId, price);
        orderCreatedListener.receiveMessage(message);
        orderRepository.findOne(Example.of(expected)).ifPresentOrElse(
                order -> assertThat(order, samePropertyValuesAs(expected)),
                () -> fail("ticket was not saved in event listener"));
    }

    @Test
    void cancels_and_saves_a_ticket() {
        String orderId = ObjectId.get().toHexString();
        Integer price = 20;
        String userId = ObjectId.get().toHexString();
        Long version = 1L;

        var newOrder = new Order(orderId, Created, price, userId);
        orderRepository.save(newOrder);

        var expected = new Order(orderId, Cancelled, price, userId);
        expected.version = version;
        var message = """
                {"id":"%s",
                "version":%s}"""
                .formatted(orderId, version);
        orderCancelledListener.receiveMessage(message);
        orderRepository.findOne(Example.of(expected)).ifPresentOrElse(
                order -> assertThat(order, samePropertyValuesAs(expected)),
                () -> fail("ticket was not saved in event listener"));
    }

}

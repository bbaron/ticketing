package ticketing.payments.messaging;


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
import ticketing.payments.Order;
import ticketing.payments.OrderRepository;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.fail;
import static ticketing.messaging.types.OrderStatus.Cancelled;
import static ticketing.messaging.types.OrderStatus.Created;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Import(TestMessagingConfiguration.class)
public class OrderListenerTests {
    final MessageIO messageIO;
    final OrderRepository orderRepository;

    @Autowired
    public OrderListenerTests(MessageIO messageIO, OrderRepository orderRepository) {
        this.messageIO = messageIO;
        this.orderRepository = orderRepository;
    }

    @Test
    void creates_and_saves_an_order() {
        String orderId = ObjectId.get()
                                 .toHexString();
        String ticketId = ObjectId.get()
                                  .toHexString();
        Instant expiresAt = Instant.now();
        OrderStatus status = Created;
        Integer price = 20;
        String userId = ObjectId.get()
                                .toHexString();
        Long version = 0L;
        var expected = Order.builder()
                            .id(orderId)
                            .price(price)
                            .status(status)
                            .version(version)
                            .userId(userId)
                            .build();

        var payload = """
                {"id":"%s",
                "status":"%s",
                "userId":"%s",
                "expiresAt":"%s",
                "version":%s,
                "ticket":{"id":"%s", "price":%s}}"""
                .formatted(orderId, status, userId, expiresAt, version, ticketId, price);
        var message = new GenericMessage<>(payload);
        messageIO.input.send(message, "orderCreated");
        orderRepository.findOne(Example.of(expected))
                       .ifPresentOrElse(
                               order -> assertThat(order, samePropertyValuesAs(expected)),
                               () -> fail("order was not saved in event listener"));

    }

    @Test
    void cancels_and_saves_order() {
        String orderId = ObjectId.get()
                                 .toHexString();
        int price = 20;
        String userId = ObjectId.get()
                                .toHexString();
        Long version = 1L;

        var newOrder = Order.of(orderId, Created, price, userId);
        orderRepository.save(newOrder);

        var expected = Order.builder()
                            .id(orderId)
                            .price(price)
                            .status(Cancelled)
                            .version(version)
                            .userId(userId)
                            .build();
        var payload = """
                {"id":"%s",
                "version":%s}"""
                .formatted(orderId, version);
        var message = new GenericMessage<>(payload);
        messageIO.input.send(message, "orderCancelled");
        orderRepository.findOne(Example.of(expected))
                       .ifPresentOrElse(
                               order -> assertThat(order, samePropertyValuesAs(expected)),
                               () -> fail("order was not saved in event listener"));

    }
}

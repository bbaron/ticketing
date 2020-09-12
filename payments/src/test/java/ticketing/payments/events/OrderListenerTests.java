package ticketing.payments.events;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//@SpringBootTest
@Disabled
class OrderListenerTests {
//    final OrderCreatedListener orderCreatedListener;
//    final OrderCancelledListener orderCancelledListener;
//    final OrderRepository orderRepository;

//    @Autowired
//    OrderListenerTests(OrderCreatedListener orderCreatedListener, OrderCancelledListener orderCancelledListener, OrderRepository orderRepository) {
//        this.orderCreatedListener = orderCreatedListener;
//        this.orderCancelledListener = orderCancelledListener;
//        this.orderRepository = orderRepository;
//    }

    @Test
    void creates_and_saves_a_ticket() {
//        String orderId = ObjectId.get().toHexString();
//        String ticketId = ObjectId.get().toHexString();
//        Instant expiresAt = Instant.now();
//        OrderStatus status = Created;
//        Integer price = 20;
//        String userId = ObjectId.get().toHexString();
//        Long version = 0L;
//
//        var expected = new Order(orderId, status, price, userId);
//        expected.version = version;
//        var message = """
//                {"id":"%s",
//                "status":"%s",
//                "userId":"%s",
//                "expiresAt":"%s",
//                "version":%s,
//                "ticket":{"id":"%s", "price":%s}}"""
//                .formatted(orderId, status, userId, expiresAt, version, ticketId, price);
//        orderCreatedListener.receiveMessage(message);
//        orderRepository.findOne(Example.of(expected)).ifPresentOrElse(
//                order -> assertThat(order, samePropertyValuesAs(expected)),
//                () -> fail("ticket was not saved in event listener"));
    }

    @Test
    void cancels_and_saves_a_ticket() {
//        String orderId = ObjectId.get().toHexString();
//        Integer price = 20;
//        String userId = ObjectId.get().toHexString();
//        Long version = 1L;
//
//        var newOrder = new Order(orderId, Created, price, userId);
//        orderRepository.save(newOrder);
//
//        var expected = new Order(orderId, Cancelled, price, userId);
//        expected.version = version;
//        var message = """
//                {"id":"%s",
//                "version":%s}"""
//                .formatted(orderId, version);
//        orderCancelledListener.receiveMessage(message);
//        orderRepository.findOne(Example.of(expected)).ifPresentOrElse(
//                order -> assertThat(order, samePropertyValuesAs(expected)),
//                () -> fail("ticket was not saved in event listener"));
    }

}

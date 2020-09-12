package ticketing.payments.messaging.listeners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import ticketing.payments.Order;
import ticketing.payments.OrderRepository;

import java.util.function.Consumer;

import static ticketing.messaging.types.OrderStatus.Cancelled;

@Configuration
@RequiredArgsConstructor
public class MessageHandlers {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrderRepository orderRepository;

    void handleOrderCreated(OrderCreatedMessage message) {
        logger.info("Received {}", message);
        var order = new Order(message.id, message.status, message.ticket.price, message.userId);
        orderRepository.save(order);
    }

    void handleOrderCancelled(OrderCancelledMessage message) {
        logger.info("Received {}", message);
        Order orderExample = new Order();
        orderExample.setVersion(message.version - 1);
        orderExample.setId(message.id);
        Example<Order> example = Example.of(orderExample);
        Order order = orderRepository.findOne(example)
                                     .orElseThrow(() -> new IllegalStateException("%s: No such order".formatted(message)));
        order.setStatus(Cancelled);
        orderRepository.save(order);
    }

    @Bean
    Consumer<OrderCreatedMessage> orderCreated() {
        return this::handleOrderCreated;
    }

    @Bean
    Consumer<OrderCancelledMessage> orderCancelled() {
        return this::handleOrderCancelled;
    }
}

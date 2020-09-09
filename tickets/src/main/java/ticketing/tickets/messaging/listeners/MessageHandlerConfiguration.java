package ticketing.tickets.messaging.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MessageHandlerConfiguration {
    private final OrderHandlers orderHandlers;

    @Bean
    public Consumer<OrderCreatedMessage> orderCreated() {
        return orderHandlers::handleOrderCreated;
    }

    @Bean
    public Consumer<OrderCancelledMessage> orderCancelled() {
        return orderHandlers::handleOrderCancelled;
    }

}

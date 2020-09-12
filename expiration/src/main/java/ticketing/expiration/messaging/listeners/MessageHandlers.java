package ticketing.expiration.messaging.listeners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.expiration.redis.RedisDelayedQueue;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MessageHandlers {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final RedisDelayedQueue redisDelayedQueue;

    void handleOrderCreated(OrderCreatedMessage message) {
        logger.info("publishing {} to redisDelayQueue", message);
        redisDelayedQueue.offerDelayedSeconds(message.id, message.calculateDelayInSeconds());
    }

    @Bean
    Consumer<OrderCreatedMessage> orderCreated() {
        return this::handleOrderCreated;
    }
}

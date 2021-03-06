package ticketing.expiration.redis;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ticketing.expiration.messaging.publishers.ExpirationCompletedMessage;
import ticketing.expiration.messaging.publishers.ExpirationCompletedPublisher;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

@Component
public class OrderExpiredListener {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String QUEUE = RedisConfiguration.REDIS_ORDER_EXPIRED_QUEUE;
    private final RedissonClient redissonClient;
    private final ExpirationCompletedPublisher expirationCompletedPublisher;

    public OrderExpiredListener(RedissonClient redissonClient, ExpirationCompletedPublisher expirationCompletedPublisher) {
        this.redissonClient = redissonClient;
        this.expirationCompletedPublisher = expirationCompletedPublisher;
    }

    @Async
    public void listen() {
        RBlockingQueue<String> queue = redissonClient.getBlockingQueue(QUEUE);
        while (true) {
            try {
                String orderId = queue.poll(1, TimeUnit.HOURS);
                if (orderId == null) {
                    logger.info("No expired orders in the last hour");
                } else if (orderId.isBlank()) {
                    break;
                } else {
                    logger.info("Order {} has expired", orderId);
                    expirationCompletedPublisher.publish(new ExpirationCompletedMessage(orderId));
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }
    }
}

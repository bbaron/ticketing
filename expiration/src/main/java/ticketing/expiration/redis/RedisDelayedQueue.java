package ticketing.expiration.redis;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisDelayedQueue {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String QUEUE = RedisConfiguration.REDIS_ORDER_EXPIRED_QUEUE;
    private final RedissonClient redissonClient;

    public RedisDelayedQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void offerDelayed(String message, long delay, TimeUnit timeUnit) {
        logger.info("delaying message '{}' for {} {}", message, delay, timeUnit.name().toLowerCase());
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(QUEUE);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        delayedQueue.offer(message, delay, timeUnit);
    }

    public void offerDelayedSeconds(String message, long delay) {
        offerDelayed(message, delay, TimeUnit.SECONDS);
    }

}

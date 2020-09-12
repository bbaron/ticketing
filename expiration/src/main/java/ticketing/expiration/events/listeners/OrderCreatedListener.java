package ticketing.expiration.events.listeners;

import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;
import ticketing.expiration.redis.RedisDelayedQueue;

//@Component
@Deprecated
public class OrderCreatedListener extends BaseListener<OrderCreatedEvent> {
    private final RedisDelayedQueue redisDelayedQueue;

    protected OrderCreatedListener(JsonOperations jsonOperations, RedisDelayedQueue redisDelayedQueue) {
        super(jsonOperations, OrderCreatedEvent.class);
        this.redisDelayedQueue = redisDelayedQueue;
    }

    @Override
    protected void onMessage(OrderCreatedEvent event) {
        logger.info("publishing {} to redisDelayQueue", event);
        redisDelayedQueue.offerDelayedSeconds(event.id, event.calculateDelayInSeconds());
    }
}

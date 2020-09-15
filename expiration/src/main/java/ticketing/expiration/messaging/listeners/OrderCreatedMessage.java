package ticketing.expiration.messaging.listeners;

import lombok.Value;

import java.time.Instant;

import static java.lang.Math.max;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Value
public class OrderCreatedMessage {
    String id;
    Instant expiresAt;

    public long calculateDelayInSeconds() {
        long delay = SECONDS.between(now(), expiresAt);
        return max(0, delay);
    }
}

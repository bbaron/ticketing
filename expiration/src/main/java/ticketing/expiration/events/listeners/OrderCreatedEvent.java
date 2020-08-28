package ticketing.expiration.events.listeners;

import ticketing.common.events.Event;

import java.time.Instant;

import static java.lang.Math.max;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;

public class OrderCreatedEvent implements Event {
    public String id;
    public Instant expiresAt;

    public OrderCreatedEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public long calculateDelayInSeconds() {
        long delay = now().until(expiresAt, SECONDS);
        return max(0, delay);
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{id='%s', expiresAt=%s}".formatted(id, expiresAt);
    }
}

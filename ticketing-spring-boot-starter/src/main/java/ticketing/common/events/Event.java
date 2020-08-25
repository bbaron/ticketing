package ticketing.common.events;

import java.util.UUID;

public interface Event {
    default String id() {
        return UUID.randomUUID().toString();
    }

    default String routingKey(Subject subject) {
        return String.format("%s.%s", subject.routingKey, id());
    }
}

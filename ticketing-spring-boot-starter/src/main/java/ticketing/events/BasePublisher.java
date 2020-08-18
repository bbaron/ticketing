package ticketing.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ticketing.json.JsonOperations;

public abstract class BasePublisher<E extends Event> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Messenger messenger;
    private final JsonOperations jsonOperations;

    private final String exchange;

    protected abstract Subject subject();

    protected BasePublisher(Messenger messenger, JsonOperations jsonOperations, String exchange) {
        this.messenger = messenger;
        this.jsonOperations = jsonOperations;
        this.exchange = exchange;
    }

    public final void publish(E event) {
        var routingKey = event.routingKey(subject());
        logger.info("publishing exchange: {}, route: {} {}", exchange, routingKey, event);
        var message = jsonOperations.writeValueAsString(event);
        logger.info("sending message: " + message);
        messenger.convertAndSend(exchange, routingKey, message);
    }
}

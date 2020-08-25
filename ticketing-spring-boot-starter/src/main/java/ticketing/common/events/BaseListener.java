package ticketing.common.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ticketing.common.json.JsonOperations;

public abstract class BaseListener<E> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final JsonOperations jsonOperations;
    private final Class<E> eventClass;

    protected BaseListener(JsonOperations jsonOperations, Class<E> eventClass) {
        this.jsonOperations = jsonOperations;
        this.eventClass = eventClass;
    }

    public void receiveMessage(String message) {
        logger.info("receiveMessage: " + message);
        E event = jsonOperations.readValue(message, eventClass);
        onMessage(event);
    }

    protected abstract void onMessage(E event);

}

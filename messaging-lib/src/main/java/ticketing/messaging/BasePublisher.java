package ticketing.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;

import static java.util.Objects.requireNonNull;

public abstract class BasePublisher<T> implements Publisher<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final StreamBridge streamBridge;
    private final String bindingName;

    protected BasePublisher(StreamBridge streamBridge, String bindingName) {
        this.streamBridge = requireNonNull(streamBridge, "streamBridge is required");
        this.bindingName = requireNonNull(bindingName, "bindingName is required")
                .endsWith("-out-0") ?
                bindingName :
                bindingName.concat("-out-0");
    }

    @Override
    public final void publish(T event) {
        if (logger.isInfoEnabled()) {
            logger.info("Publishing to channel '{}':  {}", bindingName, event);
        }
        streamBridge.send(bindingName, event);
    }
}

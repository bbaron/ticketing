package ticketing.expiration.messaging.publishers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ticketing.messaging.BasePublisher;

@Component
public class ExpirationCompletedPublisherImpl
        extends BasePublisher<ExpirationCompletedMessage>
        implements ExpirationCompletedPublisher {
    public ExpirationCompletedPublisherImpl(StreamBridge streamBridge) {
        super(streamBridge, "expirationCompleted");
    }
}

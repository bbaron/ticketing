package ticketing.payments.messaging.publishers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ticketing.messaging.BasePublisher;

@Component
public class PaymentCreatedPublisherImpl extends BasePublisher<PaymentCreatedMessage> implements PaymentCreatedPublisher {
    protected PaymentCreatedPublisherImpl(StreamBridge streamBridge) {
        super(streamBridge, "paymentCreated");
    }
}

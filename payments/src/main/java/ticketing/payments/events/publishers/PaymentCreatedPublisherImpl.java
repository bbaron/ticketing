package ticketing.payments.events.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.events.BasePublisher;
import ticketing.common.events.Messenger;
import ticketing.common.events.Subject;
import ticketing.common.json.JsonOperations;

import static ticketing.common.events.Subject.PaymentCreated;

@Component
public class PaymentCreatedPublisherImpl extends BasePublisher<PaymentCreatedEvent>
        implements PaymentCreatedPublisher {

    public PaymentCreatedPublisherImpl(Messenger messenger, JsonOperations jsonOperations, String exchange) {
        super(messenger, jsonOperations, exchange);
    }

    @Autowired
    public PaymentCreatedPublisherImpl(Messenger messenger, JsonOperations jsonOperations, TicketingProperties properties) {
        super(messenger, jsonOperations, properties);
    }

    @Override
    public Subject subject() {
        return PaymentCreated;
    }

}

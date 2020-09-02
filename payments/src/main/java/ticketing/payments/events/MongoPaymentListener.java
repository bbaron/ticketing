package ticketing.payments.events;

import org.springframework.stereotype.Component;
import ticketing.common.mongodb.AbstractOnInsertOrUpdateMongoEventListener;
import ticketing.common.mongodb.AfterInsertEvent;
import ticketing.common.mongodb.MongoTicketingListener;
import ticketing.payments.Payment;
import ticketing.payments.events.publishers.PaymentCreatedEvent;
import ticketing.payments.events.publishers.PaymentCreatedPublisher;

@Component
public class MongoPaymentListener extends AbstractOnInsertOrUpdateMongoEventListener<Payment>
        implements MongoTicketingListener<Payment> {
    private final PaymentCreatedPublisher paymentCreatedPublisher;

    public MongoPaymentListener(PaymentCreatedPublisher paymentCreatedPublisher) {
        this.paymentCreatedPublisher = paymentCreatedPublisher;
    }

    @Override
    public void onAfterInsert(AfterInsertEvent<Payment> afterInsertEvent) {
        super.onAfterInsert(afterInsertEvent);
        var payment = afterInsertEvent.getSource();
        var event = new PaymentCreatedEvent(payment.id, payment.orderId, payment.stripeId);
        paymentCreatedPublisher.publish(event);
    }
}
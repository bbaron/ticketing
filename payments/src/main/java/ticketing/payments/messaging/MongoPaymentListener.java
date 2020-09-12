package ticketing.payments.messaging;

import org.springframework.stereotype.Component;
import ticketing.common.mongodb.AbstractOnInsertOrUpdateMongoEventListener;
import ticketing.common.mongodb.AfterInsertEvent;
import ticketing.common.mongodb.MongoTicketingListener;
import ticketing.payments.Payment;
import ticketing.payments.messaging.publishers.PaymentCreatedMessage;
import ticketing.payments.messaging.publishers.PaymentCreatedPublisher;

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
        var event = new PaymentCreatedMessage(payment.id, payment.orderId, payment.stripeId);
        paymentCreatedPublisher.publish(event);
    }
}

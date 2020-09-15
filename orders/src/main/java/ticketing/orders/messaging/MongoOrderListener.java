package ticketing.orders.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ticketing.common.mongodb.AbstractOnInsertOrUpdateMongoEventListener;
import ticketing.common.mongodb.AfterInsertEvent;
import ticketing.common.mongodb.AfterUpdateEvent;
import ticketing.common.mongodb.MongoTicketingListener;
import ticketing.messaging.types.OrderStatus;
import ticketing.orders.Order;
import ticketing.orders.messaging.publishers.OrderPublisher;

@Component
@RequiredArgsConstructor
public class MongoOrderListener extends AbstractOnInsertOrUpdateMongoEventListener<Order> implements MongoTicketingListener<Order> {
    private final OrderPublisher orderPublisher;

    @Override
    public void onAfterInsert(AfterInsertEvent<Order> afterInsertEvent) {
        super.onAfterInsert(afterInsertEvent);
        var order = afterInsertEvent.getSource();
        var message = order.toOrderCreatedMessage();
        orderPublisher.publish(message);

    }

    @Override
    public void onAfterUpdate(AfterUpdateEvent<Order> afterUpdateEvent) {
        super.onAfterUpdate(afterUpdateEvent);
        var order = afterUpdateEvent.getSource();
        if (order.getStatus()
                 .equals(OrderStatus.Cancelled)) {
            orderPublisher.publish(order.toOrderCancelledMessage());
        }

    }

}

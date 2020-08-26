package ticketing.orders.events;

import org.springframework.stereotype.Component;
import ticketing.common.mongodb.AbstractOnInsertOrUpdateMongoEventListener;
import ticketing.common.mongodb.AfterInsertEvent;
import ticketing.common.mongodb.AfterUpdateEvent;
import ticketing.common.mongodb.MongoTicketingListener;
import ticketing.orders.Order;
import ticketing.orders.events.publishers.OrderCancelledEvent;
import ticketing.orders.events.publishers.OrderCancelledPublisher;
import ticketing.orders.events.publishers.OrderCreatedEvent;
import ticketing.orders.events.publishers.OrderCreatedPublisher;

@Component
public class MongoOrderListener extends AbstractOnInsertOrUpdateMongoEventListener<Order> implements MongoTicketingListener<Order> {
    private final OrderCreatedPublisher orderCreatedPublisher;
    private final OrderCancelledPublisher orderCancelledPublisher;

    public MongoOrderListener(OrderCreatedPublisher orderCreatedPublisher, OrderCancelledPublisher orderCancelledPublisher) {
        this.orderCreatedPublisher = orderCreatedPublisher;
        this.orderCancelledPublisher = orderCancelledPublisher;
    }

    @Override
    public void onAfterInsert(AfterInsertEvent<Order> afterInsertEvent) {
        super.onAfterInsert(afterInsertEvent);
        var order = afterInsertEvent.getSource();
        var ticket = order.ticket;
        var event = new OrderCreatedEvent(order.id, order.userId, order.expiration, order.version,
                order.status, ticket.id, ticket.price);
        orderCreatedPublisher.publish(event);

    }

    @Override
    public void onAfterUpdate(AfterUpdateEvent<Order> afterUpdateEvent) {
        super.onAfterUpdate(afterUpdateEvent);
        var order = afterUpdateEvent.getSource();
        var event = new OrderCancelledEvent(order.id, order.version, order.ticket.id);
        orderCancelledPublisher.publish(event);

    }
}

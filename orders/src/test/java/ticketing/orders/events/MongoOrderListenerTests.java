package ticketing.orders.events;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import ticketing.common.events.Messenger;
import ticketing.orders.Order;
import ticketing.orders.Ticket;
import ticketing.orders.events.publishers.OrderCancelledPublisher;
import ticketing.orders.events.publishers.OrderCreatedPublisher;
import ticketing.orders.events.publishers.OrderCancelledEvent;
import ticketing.orders.events.publishers.OrderCreatedEvent;

import static org.mockito.Mockito.*;
import static ticketing.messaging.types.OrderStatus.*;

@Disabled
public class MongoOrderListenerTests {
    private final Messenger messenger = mock(Messenger.class);
    private final OrderCreatedPublisher orderCreatedPublisher = mock(OrderCreatedPublisher.class);
    private final OrderCancelledPublisher orderCancelledPublisher = mock(OrderCancelledPublisher.class);
    private final MongoOrderListener listener = new MongoOrderListener(orderCreatedPublisher, orderCancelledPublisher);
    private final Order order = new Order();
    private final long updatedVersion = 1;

    @BeforeEach
    void setUp() {
        order.setId(ObjectId.get()
                            .toHexString());
        order.setTicket(new Ticket());
    }

    @Test
    @DisplayName("when order is updated and status is cancelled, emit order cancelled event")
    void test1() {
        order.setStatus(Cancelled);
        order.setVersion(updatedVersion);
        listener.onAfterSave(new AfterSaveEvent<>(order, new Document(), ""));
        verify(orderCancelledPublisher).publish(any(OrderCancelledEvent.class));
        verify(orderCreatedPublisher, never()).publish(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("when order is updated and status isn't cancelled, don't emit order cancelled event")
    void test2() {
        order.setStatus(AwaitingPayment);
        order.setVersion(updatedVersion);
        listener.onAfterSave(new AfterSaveEvent<>(order, new Document(), ""));
        verify(orderCancelledPublisher, never()).publish(any(OrderCancelledEvent.class));
        verify(orderCreatedPublisher, never()).publish(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("when order is created, emit order created event")
    void test3() {
        order.setStatus(Created);
        long createdVersion = 0;
        order.setVersion(createdVersion);
        listener.onAfterSave(new AfterSaveEvent<>(order, new Document(), ""));
        verify(orderCancelledPublisher, never()).publish(any(OrderCancelledEvent.class));
        verify(orderCreatedPublisher).publish(any(OrderCreatedEvent.class));
    }
}

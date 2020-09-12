package ticketing.orders.events.listeners.paymentcreated;

import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.messaging.types.OrderStatus;
import ticketing.common.json.JsonOperations;
import ticketing.orders.Order;
import ticketing.orders.OrderRepository;

//@Component
@Deprecated
public class PaymentCreatedListener extends BaseListener<PaymentCreatedEvent> {
    private final OrderRepository orderRepository;

    public PaymentCreatedListener(JsonOperations jsonOperations, OrderRepository orderRepository) {
        super(jsonOperations, PaymentCreatedEvent.class);
        this.orderRepository = orderRepository;
    }

    @Override
    protected void onMessage(PaymentCreatedEvent event) {
//        Order order = orderRepository.findById(event.orderId)
//                .orElseThrow(() -> new IllegalStateException("Order not found " + event));
//        order.setStatus(OrderStatus.Complete);
//        logger.info("Payment received on {}", order);
//        orderRepository.save(order);
    }
}

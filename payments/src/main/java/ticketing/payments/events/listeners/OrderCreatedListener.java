package ticketing.payments.events.listeners;

import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;
import ticketing.payments.Order;
import ticketing.payments.OrderRepository;


@Component
public class OrderCreatedListener extends BaseListener<OrderCreatedEvent> {
    private final OrderRepository orderRepository;
    public OrderCreatedListener(JsonOperations jsonOperations, OrderRepository orderRepository) {
        super(jsonOperations, OrderCreatedEvent.class);
        this.orderRepository = orderRepository;
    }

    @Override
    protected void onMessage(OrderCreatedEvent event) {
        var order = new Order(event.id, event.status, event.ticket.price, event.userId);
        orderRepository.save(order);
    }
}

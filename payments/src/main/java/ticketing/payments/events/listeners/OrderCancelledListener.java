package ticketing.payments.events.listeners;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;
import ticketing.payments.Order;
import ticketing.payments.OrderRepository;

import static ticketing.common.events.types.OrderStatus.Cancelled;

@Component
public class OrderCancelledListener extends BaseListener<OrderCancelledEvent> {
    private final OrderRepository orderRepository;

    public OrderCancelledListener(JsonOperations jsonOperations, OrderRepository orderRepository) {
        super(jsonOperations, OrderCancelledEvent.class);
        this.orderRepository = orderRepository;
    }

    @Override
    protected void onMessage(OrderCancelledEvent event) {

        Order orderExample = new Order();
        orderExample.setVersion(event.version - 1);
        orderExample.setId(event.id);
        Example<Order> example = Example.of(orderExample);
        Order order = orderRepository.findOne(example)
                .orElseThrow(() -> new IllegalStateException("%s: No such order".formatted(event)));
        order.setStatus(Cancelled);
        orderRepository.save(order);
    }
}

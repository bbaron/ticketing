package ticketing.orders.events.listeners.expirationcompleted;

import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.events.types.OrderStatus;
import ticketing.common.json.JsonOperations;
import ticketing.orders.OrderRepository;

import static ticketing.common.events.types.OrderStatus.*;

@Component
public class ExpirationCompletedListener extends BaseListener<ExpirationCompletedEvent> {
    private final OrderRepository orderRepository;

    public ExpirationCompletedListener(JsonOperations jsonOperations, OrderRepository orderRepository) {
        super(jsonOperations, ExpirationCompletedEvent.class);
        this.orderRepository = orderRepository;
    }

    @Override
    protected void onMessage(ExpirationCompletedEvent event) {
        var order = orderRepository.findById(event.orderId)
                .orElseThrow(() -> new IllegalStateException(event + " order not found"));
        if (order.status != Complete) {
            order.setStatus(Cancelled);
            orderRepository.save(order);
        }
    }
}
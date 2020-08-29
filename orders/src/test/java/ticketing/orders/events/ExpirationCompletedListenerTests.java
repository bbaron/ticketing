package ticketing.orders.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ticketing.common.json.JacksonJsonOperations;
import ticketing.common.json.JsonOperations;
import ticketing.orders.Order;
import ticketing.orders.OrderRepository;
import ticketing.orders.Ticket;
import ticketing.orders.events.listeners.expirationcompleted.ExpirationCompletedEvent;
import ticketing.orders.events.listeners.expirationcompleted.ExpirationCompletedListener;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static ticketing.common.events.types.OrderStatus.*;

public class ExpirationCompletedListenerTests {
    private final JsonOperations jsonOperations = new JacksonJsonOperations(new ObjectMapper());
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final ExpirationCompletedListener listener = new ExpirationCompletedListener(jsonOperations, orderRepository);
    private final String orderId = ObjectId.get().toHexString();
    private final Ticket ticket = new Ticket();
    private final Order order = new Order();
    private final String message = """
            {"orderId":"%s"}""".formatted(orderId);
    private final ExpirationCompletedEvent event = new ExpirationCompletedEvent(orderId);

    @BeforeEach
    void setUp() {
        order.setId(orderId);
        ticket.setOrderId(orderId);
        ticket.setTitle("concert");
        ticket.setPrice(20);
        order.setTicket(ticket);
        order.setExpiration(Instant.now());
    }

    @Test
    @DisplayName("updates the order status to cancelled")
    void test1() {
        order.setStatus(Created);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        listener.receiveMessage(message);
        verify(orderRepository).save(argThat(hasProperty("status", equalTo(Cancelled))));
    }

    @Test
    @DisplayName("does not cancel a completed order")
    void test() {
        order.setStatus(Complete);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        listener.receiveMessage(message);
        verify(orderRepository, never()).save(any());
    }
}

package ticketing.orders.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ticketing.orders.Order;
import ticketing.orders.Order.OrderBuilder;
import ticketing.orders.OrderRepository;
import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;
import ticketing.orders.messaging.publishers.OrderCancelledPublisher;
import ticketing.orders.messaging.publishers.OrderCreatedPublisher;

import static org.mockito.Mockito.*;
import static ticketing.messaging.types.OrderStatus.*;

@SpringBootTest
class MongoOrderListenerTests {
    final OrderRepository orderRepository;
    final TicketRepository ticketRepository;
    @MockBean
    OrderCreatedPublisher orderCreatedPublisher;
    @MockBean
    OrderCancelledPublisher orderCancelledPublisher;
    final OrderBuilder order = Order.builder();

    @Autowired
    MongoOrderListenerTests(OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @BeforeEach
    void setUp() {
        var ticket = Ticket.builder();
        var saved = ticketRepository.insert(ticket.price(10)
                                                  .title("event")
                                                  .build());
        order.ticket(saved)
             .status(Created);
    }

    @Test
    @DisplayName("when order is created, emit order created event")
    void test1() {
        orderRepository.insert(order.build());
        verify(orderCreatedPublisher).publish(any());
        verify(orderCancelledPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("when order is updated and status is cancelled, emit order cancelled event")
    void test2() {
        var inserted = orderRepository.insert(order.build());
        reset(orderCreatedPublisher);
        orderRepository.save(inserted.withStatus(Cancelled));
        verify(orderCancelledPublisher).publish(any());
        verify(orderCreatedPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("when order is updated and status is not cancelled, order cancelled is not emitted")
    void test3() {
        var inserted = orderRepository.insert(order.build());
        reset(orderCreatedPublisher);
        orderRepository.save(inserted.withStatus(AwaitingPayment));
        verify(orderCancelledPublisher, never()).publish(any());
        verify(orderCreatedPublisher, never()).publish(any());
    }
}

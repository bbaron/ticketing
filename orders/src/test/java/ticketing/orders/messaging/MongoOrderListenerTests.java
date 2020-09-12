package ticketing.orders.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ticketing.orders.Order;
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
    final Order order = new Order();
    final Ticket ticket = new Ticket();

    @Autowired
    MongoOrderListenerTests(OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @BeforeEach
    void setUp() {
        ticket.setPrice(10);
        ticket.setTitle("event");
        ticketRepository.insert(ticket);
        order.setTicket(ticket);
        order.setStatus(Created);
    }

    @Test
    @DisplayName("when order is created, emit order created event")
    void test1() {
        orderRepository.insert(order);
        verify(orderCreatedPublisher).publish(any());
        verify(orderCancelledPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("when order is updated and status is cancelled, emit order cancelled event")
    void test2() {
        orderRepository.insert(order);
        reset(orderCreatedPublisher);
        order.setStatus(Cancelled);
        orderRepository.save(order);
        verify(orderCancelledPublisher).publish(any());
        verify(orderCreatedPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("when order is updated and status is not cancelled, order cancelled is not emitted")
    void test3() {
        orderRepository.insert(order);
        reset(orderCreatedPublisher);
        order.setStatus(AwaitingPayment);
        orderRepository.save(order);
        verify(orderCancelledPublisher, never()).publish(any());
        verify(orderCreatedPublisher, never()).publish(any());
    }
}

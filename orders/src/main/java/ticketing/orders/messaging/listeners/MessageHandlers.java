package ticketing.orders.messaging.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import ticketing.orders.Order;
import ticketing.orders.OrderRepository;
import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;

import java.util.function.Consumer;

import static ticketing.messaging.types.OrderStatus.Cancelled;
import static ticketing.messaging.types.OrderStatus.Completed;

@Configuration
@RequiredArgsConstructor
public class MessageHandlers {
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;

    void handleTicketCreated(TicketCreatedMessage message) {
        Ticket ticket = new Ticket(message.id, message.title, message.price);
        ticketRepository.save(ticket);
    }

    void handleTicketUpdated(TicketUpdatedMessage message) {
        var example = new Ticket();
        example.setId(message.id);
        example.setVersion(message.version - 1);
        Ticket ticket = ticketRepository.findOne(Example.of(example))
                                        .orElseThrow(() -> new IllegalStateException("ticket not found from " + message));
        ticket.setTitle(message.title);
        ticket.setPrice(message.price);
        ticket.setOrderId(message.orderId);
        ticketRepository.save(ticket);
    }

    void handleExpirationCompleted(ExpirationCompletedMessage message) {
        var order = orderRepository.findById(message.orderId)
                                   .orElseThrow(() -> new IllegalStateException(message + " order not found"));
        if (order.status != Completed) {
            order.setStatus(Cancelled);
            orderRepository.save(order);
        }
    }

    void handlePaymentCreated(PaymentCreatedMessage message) {
        Order order = orderRepository.findById(message.orderId)
                                     .orElseThrow(() -> new IllegalStateException("Order not found " + message));
        order.setStatus(Completed);
        orderRepository.save(order);
    }

    @Bean
    public Consumer<TicketCreatedMessage> ticketCreated() {
        return this::handleTicketCreated;
    }

    @Bean
    public Consumer<TicketUpdatedMessage> ticketUpdated() {
        return this::handleTicketUpdated;
    }

    @Bean
    public Consumer<ExpirationCompletedMessage> expirationCompleted() {
        return this::handleExpirationCompleted;
    }

    @Bean
    public Consumer<PaymentCreatedMessage> paymentCreated() {
        return this::handlePaymentCreated;
    }

}

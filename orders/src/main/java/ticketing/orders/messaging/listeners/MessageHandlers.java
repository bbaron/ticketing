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
        Ticket ticket = Ticket.of(message.getId(), message.getTitle(), message.getPrice());
        ticketRepository.save(ticket);
    }

    void handleTicketUpdated(TicketUpdatedMessage message) {
        var example = Ticket.builder()
                            .id(message.getId())
                            .version(message.getVersion() - 1)
                            .build();
        Ticket ticket = ticketRepository.findOne(Example.of(example))
                                        .orElseThrow(() -> new IllegalStateException("ticket not found from " + message));
        var updated = ticket
                .withTitle(message.getTitle())
                .withPrice(message.getPrice())
                .withOrderId(message.getOrderId());
        ticketRepository.save(updated);
    }

    void handleExpirationCompleted(ExpirationCompletedMessage message) {
        var order = orderRepository.findById(message.getOrderId())
                                   .orElseThrow(() -> new IllegalStateException(message + " order not found"));
        if (order.getStatus() != Completed) {
            orderRepository.save(order.withStatus(Cancelled));
        }
    }

    void handlePaymentCreated(PaymentCreatedMessage message) {
        Order order = orderRepository.findById(message.getOrderId())
                                     .orElseThrow(() -> new IllegalStateException("Order not found " + message));
        orderRepository.save(order.withStatus(Completed));
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

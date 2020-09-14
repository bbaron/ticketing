package ticketing.tickets.messaging.listeners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;

@Service
@RequiredArgsConstructor
public class OrderHandlers {
    protected final Logger logger = LoggerFactory.getLogger(OrderHandlers.class);
    private final TicketRepository ticketRepository;

    public void handleOrderCreated(OrderCreatedMessage message) {
        // find ticket
        ticketRepository.findById(message.getTicket().getId())
                        .ifPresentOrElse(
                                ticket -> {
                                    // mark ticket reserved by setting orderId property
                                    var updated = ticket.withOrderId(message.getId());

                                    logger.info("received: {} updating {}", message, updated);

                                    // save ticket
                                    ticketRepository.save(updated);
                                },
                                () -> {
                                    throw new IllegalStateException("%s: No such ticket".formatted(message));
                                }
                        );
    }
    public void handleOrderCancelled(OrderCancelledMessage message) {
        Ticket ticket = ticketRepository.findById(message.getTicket().getId())
                                        .orElseThrow(() -> new IllegalStateException("%s: No such ticket".formatted(message)));
        ticket = ticket.withoutOrderId();
        ticketRepository.save(ticket);

    }


}

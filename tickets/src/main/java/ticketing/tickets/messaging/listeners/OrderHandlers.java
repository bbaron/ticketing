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
        ticketRepository.findById(message.ticket.id)
                        .ifPresentOrElse(
                                ticket -> {
                                    // mark ticket reserved by setting orderId property
                                    ticket = ticket.withOrderId(message.id);

                                    logger.info("received: {} updating {}", message, ticket);

                                    // save ticket
                                    ticketRepository.save(ticket);
                                },
                                () -> logger.error("{}: No such ticket", message)
                        );
    }
    public void handleOrderCancelled(OrderCancelledMessage event) {
        Ticket ticket = ticketRepository.findById(event.ticket.id)
                                        .orElseThrow(() -> new IllegalStateException("%s: No such ticket".formatted(event)));
        ticket = ticket.withoutOrderId();
        ticketRepository.save(ticket);

    }


}

package ticketing.tickets.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ticketing.tickets.TicketRepository;
import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;

//@Component
@Deprecated
public class OrderCreatedListener extends BaseListener<OrderCreatedEvent> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final TicketRepository ticketRepository;
    public OrderCreatedListener(JsonOperations jsonOperations, TicketRepository ticketRepository) {
        super(jsonOperations, OrderCreatedEvent.class);
        this.ticketRepository = ticketRepository;
    }

    @Override
    protected void onMessage(OrderCreatedEvent event) {
        // find ticket
        ticketRepository.findById(event.ticket.id).ifPresentOrElse(
                ticket -> {
                    // mark ticket reserved by setting orderId property
                    ticket = ticket.withOrderId(event.id);

                    logger.info("received: {} updating {}", event, ticket);

                    // save ticket
                    ticketRepository.save(ticket);
                },
                () -> logger.error("{}: No such ticket", event)
        );

    }
}

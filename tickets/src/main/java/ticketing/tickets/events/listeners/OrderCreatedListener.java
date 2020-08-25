package ticketing.tickets.events.listeners;

import ticketing.tickets.TicketRepository;
import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;

@Component
public class OrderCreatedListener extends BaseListener<OrderCreatedEvent> {
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

                    // save ticket
                    ticketRepository.save(ticket);
                },
                () -> logger.error("{}: No such ticket", event)
        );

    }
}

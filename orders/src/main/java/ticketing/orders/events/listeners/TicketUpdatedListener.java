package ticketing.orders.events.listeners;

import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;
import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;

@Component
public class TicketUpdatedListener extends BaseListener<TicketUpdatedEvent> {
    private final TicketRepository ticketRepository;

    public TicketUpdatedListener(JsonOperations jsonOperations, TicketRepository ticketRepository) {
        super(jsonOperations, TicketUpdatedEvent.class);
        this.ticketRepository = ticketRepository;
    }

    @Override
    protected void onMessage(TicketUpdatedEvent event) {
        logger.info("onMessage: " + event);
        Ticket ticket = ticketRepository.findDistinctByIdAndVersion(event.id, event.version - 1)
                .orElseThrow(() -> new IllegalStateException("ticket not found from " + event));
        ticket.setTitle(event.title);
        ticket.setPrice(event.price);
        ticketRepository.save(ticket);
    }
}

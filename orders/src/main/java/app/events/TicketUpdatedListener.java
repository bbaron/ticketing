package app.events;

import app.Ticket;
import app.TicketRepository;
import org.springframework.stereotype.Component;
import ticketing.events.BaseListener;
import ticketing.json.JsonOperations;

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

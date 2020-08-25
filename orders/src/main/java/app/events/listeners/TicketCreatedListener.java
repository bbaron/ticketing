package app.events.listeners;

import app.Ticket;
import app.TicketRepository;
import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;

@Component
public class TicketCreatedListener extends BaseListener<TicketCreatedEvent> {
    private final TicketRepository ticketRepository;

    public TicketCreatedListener(JsonOperations jsonOperations, TicketRepository ticketRepository) {
        super(jsonOperations, TicketCreatedEvent.class);
        this.ticketRepository = ticketRepository;
    }

    @Override
    protected void onMessage(TicketCreatedEvent event) {
        logger.info("onMessage: " + event);
        Ticket ticket = new Ticket(event.id, event.title, event.price);
        ticketRepository.save(ticket);
    }
}

package app.events;

import app.Ticket;
import app.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ticketing.events.BaseListener;
import ticketing.json.JsonOperations;

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

package ticketing.tickets.events.listeners;

import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;
import ticketing.tickets.Ticket;
import ticketing.tickets.TicketRepository;

//@Component
@Deprecated
public class OrderCancelledListener extends BaseListener<OrderCancelledEvent> {
    private final TicketRepository ticketRepository;
    public OrderCancelledListener(JsonOperations jsonOperations, TicketRepository ticketRepository) {
        super(jsonOperations, OrderCancelledEvent.class);
        this.ticketRepository = ticketRepository;
    }

    @Override
    protected void onMessage(OrderCancelledEvent event) {
        Ticket ticket = ticketRepository.findById(event.ticket.id)
                .orElseThrow(() -> new IllegalStateException("%s: No such ticket".formatted(event)));
        ticket = ticket.withoutOrderId();
        ticketRepository.save(ticket);
    }
}

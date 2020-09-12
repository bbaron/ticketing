package ticketing.orders.events.listeners.ticketupdated;

import org.springframework.data.domain.Example;
import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;
import org.springframework.stereotype.Component;
import ticketing.common.events.BaseListener;
import ticketing.common.json.JsonOperations;

//@Component
@Deprecated
public class TicketUpdatedListener extends BaseListener<TicketUpdatedEvent> {
    private final TicketRepository ticketRepository;

    public TicketUpdatedListener(JsonOperations jsonOperations, TicketRepository ticketRepository) {
        super(jsonOperations, TicketUpdatedEvent.class);
        this.ticketRepository = ticketRepository;
    }

    @Override
    protected void onMessage(TicketUpdatedEvent event) {
//        var example = new Ticket();
//        example.setId(event.id);
//        example.setVersion(event.version - 1);
//        Ticket ticket = ticketRepository.findOne(Example.of(example))
//                .orElseThrow(() -> new IllegalStateException("ticket not found from " + event));
//        ticket.setTitle(event.title);
//        ticket.setPrice(event.price);
//        ticket.setOrderId(event.orderId);
//        ticketRepository.save(ticket);
    }
}

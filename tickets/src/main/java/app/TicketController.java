package app;

import app.events.TicketCreatedEvent;
import app.events.TicketCreatedPublisher;
import app.events.TicketUpdatedEvent;
import app.events.TicketUpdatedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.exceptions.ForbiddenException;
import ticketing.exceptions.NotFoundException;
import ticketing.exceptions.RequestValidationException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = {"/api/tickets", "/", ""})
public class TicketController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TicketRepository ticketRepository;
    private final TicketCreatedPublisher ticketCreatedPublisher;
    private final TicketUpdatedPublisher ticketUpdatedPublisher;

    public TicketController(TicketRepository ticketRepository,
                            TicketCreatedPublisher ticketCreatedPublisher,
                            TicketUpdatedPublisher ticketUpdatedPublisher) {
        this.ticketRepository = ticketRepository;
        this.ticketCreatedPublisher = ticketCreatedPublisher;
        this.ticketUpdatedPublisher = ticketUpdatedPublisher;
    }

    @GetMapping
    List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    Ticket findById(@PathVariable String id) {
        return ticketRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    ResponseEntity<Ticket> create(@RequestBody @Valid TicketRequest request, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            throw new RequestValidationException(result);
        }
        var ticket = new Ticket(request, principal.getName());
        ticket = ticketRepository.save(ticket);
        logger.info("saved {}", ticket);
        var event = new TicketCreatedEvent(ticket.id, ticket.title, ticket.userId, ticket.price, ticket.version);
        ticketCreatedPublisher.publish(event);
        return status(CREATED).body(ticket);
    }

    @PutMapping("/{id}")
    ResponseEntity<Ticket> update(@RequestBody @Valid TicketRequest request,
                                  BindingResult result,
                                  @PathVariable String id, Principal principal) {
        if (result.hasErrors()) {
            throw new RequestValidationException(result);
        }
        var ticket = ticketRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!Objects.equals(ticket.getUserId(), principal.getName())) {
            throw new ForbiddenException();
        }
        ticket = ticket.update(request.getTitle(), request.getPrice());
        ticket = ticketRepository.save(ticket);
        logger.info("updated {}", ticket);
        var event = new TicketUpdatedEvent(ticket.id, ticket.title, ticket.userId, ticket.price, ticket.version);
        ticketUpdatedPublisher.publish(event);
        return ok(ticket);
    }

}

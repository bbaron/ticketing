package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = "/api/tickets")
public class TicketController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    Ticket findById(@PathVariable String id) {
        return ticketRepository.findById(id).orElseThrow(() -> new NotFoundException(id + ": no such ticket"));
    }

    @PostMapping
    ResponseEntity<Ticket> create(@RequestBody @Valid TicketRequest request, Principal principal) {
        var ticket = new Ticket(request, principal.getName());
        ticket = ticketRepository.save(ticket);
        logger.info("saved {}", ticket);
        return status(CREATED).body(ticket);
    }

    @PutMapping("/{id}")
    ResponseEntity<Ticket> update(@RequestBody @Valid TicketRequest request, @PathVariable String id, Principal principal) {
        var ticket = ticketRepository.findById(id).orElseThrow(() -> new NotFoundException(id + ": no such ticket"));
        if (!Objects.equals(ticket.getUserId(), principal.getName())) {
            throw new ForbiddenException();
        }
        ticket.setPrice(request.getPrice());
        ticket.setTitle(request.getTitle());
        ticket = ticketRepository.save(ticket);
        logger.info("updated {}", ticket);
        return ok(ticket);
    }
}

package ticketing.tickets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.common.exceptions.ForbiddenException;
import ticketing.common.exceptions.NotFoundException;
import ticketing.common.exceptions.RequestValidationException;

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

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
        ticket = ticketRepository.insert(ticket);
        logger.info("saved {}", ticket);
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
        return ok(ticket);
    }

}

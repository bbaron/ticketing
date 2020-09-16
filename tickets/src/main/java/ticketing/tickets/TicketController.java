package ticketing.tickets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.common.exceptions.BadRequestException;
import ticketing.common.exceptions.ForbiddenException;
import ticketing.common.exceptions.NotFoundException;
import ticketing.common.exceptions.RequestValidationException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
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
    TicketsResponse findAll() {

        var tickets = ticketRepository
                .findByOrderIdIsNull()
                .stream()
                .map(Ticket::toTicketResponse)
                .collect(toList());
        var response = new TicketsResponse(tickets);
        logger.info("GET /api/tickets: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    TicketResponse findById(@PathVariable String id) {
        return ticketRepository.findById(id)
                               .map(Ticket::toTicketResponse)
                               .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    ResponseEntity<TicketResponse> create(@RequestBody @Valid TicketRequest request, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            throw new RequestValidationException(result);
        }
        var ticket = Ticket.of(request.getTitle(), request.getPrice(), principal.getName());
        ticket = ticketRepository.insert(ticket);
        logger.info("saved {}", ticket);
        return status(CREATED).body(ticket.toTicketResponse());
    }

    @PutMapping("/{id}")
    ResponseEntity<TicketResponse> update(@RequestBody @Valid TicketRequest request,
                                          BindingResult result,
                                          @PathVariable String id, Principal principal) {
        if (result.hasErrors()) {
            throw new RequestValidationException(result);
        }
        var ticket = ticketRepository.findById(id)
                                     .orElseThrow(NotFoundException::new);
        if (ticket.reserved()) {
            throw new BadRequestException("Cannot edit a reserved ticket");
        }
        if (!Objects.equals(ticket.getUserId(), principal.getName())) {
            throw new ForbiddenException();
        }
        var updated = ticket.withTitle(request.getTitle())
                       .withPrice(request.getPrice());
        var saved = ticketRepository.save(updated);
        logger.info("updated {}", saved);
        return ok(saved.toTicketResponse());
    }

}

package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.exceptions.ForbiddenException;
import ticketing.exceptions.NotFoundException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = "/api/tickets")
public class TicketController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TicketRepository ticketRepository;
    private final Environment env;
    private final DiscoveryClient discoveryClient;

    public TicketController(TicketRepository ticketRepository, Environment env, DiscoveryClient discoveryClient) {
        this.ticketRepository = ticketRepository;
        this.env = env;
        this.discoveryClient = discoveryClient;
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
    ResponseEntity<Ticket> create(@RequestBody @Valid TicketRequest request, Principal principal) {
        var ticket = new Ticket(request, principal.getName());
        ticket = ticketRepository.save(ticket);
        logger.info("saved {}", ticket);
        return status(CREATED).body(ticket);
    }

    @PutMapping("/{id}")
    ResponseEntity<Ticket> update(@RequestBody @Valid TicketRequest request, @PathVariable String id, Principal principal) {
        var ticket = ticketRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!Objects.equals(ticket.userId(), principal.getName())) {
            throw new ForbiddenException();
        }
        ticket = ticket.update(request.getTitle(), request.getPrice());
        ticket = ticketRepository.save(ticket);
        logger.info("updated {}", ticket);
        return ok(ticket);
    }

    @GetMapping("/messages")
    Map<String, String> getMessages() {
        return Map.of("message", env.getProperty("message", "No message"),
                "ticket-message", env.getProperty("ticket-message", "No ticket message"));
    }

    @GetMapping("/service-instances/{appName}")
    List<ServiceInstance> serviceInstancesByAppName(@PathVariable String appName) {
        return this.discoveryClient.getInstances(appName);
    }

}

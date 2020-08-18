package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.events.types.OrderStatus;
import ticketing.exceptions.BadRequestException;
import ticketing.exceptions.ForbiddenException;
import ticketing.exceptions.NotFoundException;
import ticketing.exceptions.RequestValidationException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = {"/api/orders", "/", ""})
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final int EXPIRATION_WINDOW_SECONDS = 60 * 15;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    public OrderController(OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    OrdersResponse findAll(Principal principal) {
        var orders = orderRepository.findByUserId(principal.getName())
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return new OrdersResponse(orders);
    }

    @RequestMapping(path = "/{id}", method = {GET, DELETE})
    OrderResponse findOrCancel(@PathVariable String id, Principal principal, HttpMethod method) {
        var order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!Objects.equals(order.getUserId(), principal.getName())) {
            throw new ForbiddenException();
        }
        if (method.equals(HttpMethod.DELETE)) {
            order.setStatus(OrderStatus.Cancelled);
            orderRepository.save(order);
        }
        return new OrderResponse(order);
    }

    @PostMapping()
    ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest orderRequest,
                                         BindingResult bindingResult,
                                         Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        var ticket = ticketRepository.findById(orderRequest.ticketId()).orElseThrow(NotFoundException::new);
        boolean reserved = orderRepository.findByTicket(ticket)
                .stream()
                .anyMatch(o -> o.getStatus().isReserved());
        if (reserved) {
            throw new BadRequestException("Ticket is reserved");
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, EXPIRATION_WINDOW_SECONDS);
        Date expiration = cal.getTime();
        logger.info("expiration: " + expiration);
        var userId = principal.getName();
        var order = new Order(userId, OrderStatus.Created, expiration, ticket);
        order = orderRepository.save(order);
        logger.info("Saved " + order);

        return status(CREATED).body(new OrderResponse(order));
    }
}

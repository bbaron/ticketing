package ticketing.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.exceptions.BadRequestException;
import ticketing.common.exceptions.ForbiddenException;
import ticketing.common.exceptions.NotFoundException;
import ticketing.common.exceptions.RequestValidationException;
import ticketing.common.oauth.CurrentUser;
import ticketing.messaging.types.OrderStatus;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = {"/api/orders", "/", ""})
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final long expirationWindowSeconds;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    public OrderController(OrderRepository orderRepository,
                           TicketRepository ticketRepository,
                           TicketingProperties ticketingProperties) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.expirationWindowSeconds = ticketingProperties.orders.expirationWindowSeconds;
    }

    @GetMapping
    OrdersResponse findAll(CurrentUser currentUser) {
        var orders = orderRepository.findByUserId(currentUser.getId())
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return new OrdersResponse(orders);
    }

    @RequestMapping(path = "/{id}", method = {GET, DELETE})
    OrderResponse findOrCancel(@PathVariable String id, CurrentUser currentUser, HttpMethod method) {
        var order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!Objects.equals(order.getUserId(), currentUser.getId())) {
            throw new ForbiddenException();
        }
        if (method.equals(HttpMethod.DELETE)) {
            order = order.withStatus(OrderStatus.Cancelled);
            orderRepository.save(order);
        }
        return OrderResponse.of(order);
    }

    @PostMapping
    ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest orderRequest,
                                         BindingResult bindingResult,
                                         CurrentUser currentUser,
                                         @RequestParam(value = "expiration", required = false) Long expirationSeconds) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        var ticket = ticketRepository.findById(orderRequest.getTicketId()).orElse(null);
        if (ticket == null) {
            logger.warn("No ticket found request: {}", orderRequest);
            throw new NotFoundException();
        }
        boolean reserved = orderRepository.findByTicket(ticket)
                .stream()
                .anyMatch(o -> o.getStatus().isReserved());
        if (reserved) {
            throw new BadRequestException("Ticket is reserved");
        }
        if (expirationSeconds != null) {
            logger.info("Custom expiration of {} seconds", expirationSeconds);
        }
        long seconds = requireNonNullElse(expirationSeconds, expirationWindowSeconds);
        var expiration = Instant.now().plusSeconds(seconds);
        logger.info("expiration: " + expiration);
        var userId = currentUser.getId();
        var order = Order.of(userId, OrderStatus.Created, expiration, ticket);
        order = orderRepository.insert(order);
        logger.info("Saved " + order);

        return status(CREATED).body(OrderResponse.of(order));
    }
}

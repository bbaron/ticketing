package app;

import app.events.publishers.OrderCancelledEvent;
import app.events.publishers.OrderCancelledPublisher;
import app.events.publishers.OrderCreatedEvent;
import app.events.publishers.OrderCreatedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.common.events.types.OrderStatus;
import ticketing.common.exceptions.BadRequestException;
import ticketing.common.exceptions.ForbiddenException;
import ticketing.common.exceptions.NotFoundException;
import ticketing.common.exceptions.RequestValidationException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
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
    private final OrderCreatedPublisher orderCreatedPublisher;
    private final OrderCancelledPublisher orderCancelledPublisher;

    public OrderController(OrderRepository orderRepository, TicketRepository ticketRepository,
                           OrderCreatedPublisher orderCreatedPublisher, OrderCancelledPublisher orderCancelledPublisher) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.orderCreatedPublisher = orderCreatedPublisher;
        this.orderCancelledPublisher = orderCancelledPublisher;
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
            var event = new OrderCancelledEvent(order.id, order.version, order.ticket.id);
            orderCancelledPublisher.publish(event);
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
        order = orderRepository.insert(order);
        logger.info("Saved " + order);

        var event = new OrderCreatedEvent(order.id, order.userId, order.expiration, order.version,
                order.status, ticket.id, ticket.price);
        orderCreatedPublisher.publish(event);

        return status(CREATED).body(new OrderResponse(order));
    }
}

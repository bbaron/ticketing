package ticketing.orders;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.test.MockMvcSetup;
import ticketing.messaging.types.OrderStatus;
import ticketing.orders.messaging.publishers.OrderCreatedPublisher;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
@Disabled
class OrderCreateTestsDisabled {
    final MockMvc mvc;
    final TicketRepository ticketRepository;
    final OrderRepository orderRepository;
    @MockBean
    OrderCreatedPublisher orderCreatedPublisher;

    @Autowired
    OrderCreateTestsDisabled(MockMvc mvc, TicketRepository ticketRepository, OrderRepository orderRepository) {
        this.mvc = mvc;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
    }

    @Test
    @DisplayName("ticketId must be a valid mongo id")
    @WithMockUser
    void ticketId_must_be_a_valid_mongo_id() throws Exception {
        var ticketId = "not a valid id";
        var content = """
                {"ticketId": "%s"}
                """.formatted(ticketId);
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(orderCreatedPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("errors if ticket not found")
    @WithMockUser
    void errors_if_ticket_not_found() throws Exception {
        var ticketId = ObjectId.get().toHexString();
        var content = """
                {"ticketId": "%s"}
                """.formatted(ticketId);
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(orderCreatedPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("errors if ticket is reserved")
    @WithMockUser
    void errors_if_ticket_is_reserved() throws Exception {
        var ticket = new Ticket(null, "concert", 20);
        ticket = ticketRepository.save(ticket);
        var ticketId = ticket.getId();
        var order = new Order("user", OrderStatus.Created, Instant.now(), ticket);
        orderRepository.save(order);

        var content = """
                {"ticketId": "%s"}
                """.formatted(ticketId);
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("reserves a ticket")
    @WithMockUser
    void reserves_a_ticket() throws Exception {
        var ticket = new Ticket(null, "concert", 20);
        ticket = ticketRepository.save(ticket);

        var content = """
                {"ticketId": "%s"}
                """.formatted(ticket.getId());
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(orderCreatedPublisher).publish(any());
    }

}

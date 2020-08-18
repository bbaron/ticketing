package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.events.types.OrderStatus;
import ticketing.test.MockMvcSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcSetup
class OrderShowTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ObjectMapper objectMapper;

    private Ticket buildTicket() {
        var ticket = new Ticket("concert", 20);
        return ticketRepository.save(ticket);
    }

    private OrderResponse createOrder(String userId) throws Exception {
        var ticket = buildTicket();
        var content = """
                {"ticketId": "%s"}
                """.formatted(ticket.getId());
        var json = mvc.perform(post("/api/orders")
                .with(user(userId))
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(json, OrderResponse.class);
    }

    @Test
    @DisplayName("fetches orders for a user")
    void fetches_orders_for_a_user() throws Exception {
        var u1 = "user1";
        var u2 = "user2";
        createOrder(u1);
        var o0 = createOrder(u2);
        var o1 = createOrder(u2);
        mvc.perform(get("/api/orders")
                .with(user(u2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.orders[0].id").value(o0.getId()))
                .andExpect(jsonPath("$.orders[1].id").value(o1.getId()))
        ;
    }

    @Test
    @DisplayName("fetches order for a user")
    void fetches_order_for_a_user() throws Exception {
        var u = "user1";
        var o = createOrder(u);
        mvc.perform(get("/api/orders/{id}", o.getId())
                .with(user(u)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(o.getId()))
                .andExpect(jsonPath("$.ticket.title").value(o.getTicket().getTitle()))
        ;
    }

    @Test
    @DisplayName("cancels order for a user")
    void cancels_order_for_a_user() throws Exception {
        var u = "user1";
        var o = createOrder(u);
        mvc.perform(delete("/api/orders/{id}", o.getId())
                .with(user(u)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(o.getId()))
                .andExpect(jsonPath("$.ticket.title").value(o.getTicket().getTitle()))
        ;
        var updated = orderRepository.findById(o.getId()).orElseThrow();
        assertEquals(OrderStatus.Cancelled, updated.getStatus());
    }

    @Test
    @DisplayName("error user not owner of order")
    void error_user_not_owner_of_order() throws Exception {
        var u1 = "user1";
        var u2 = "user2";
        var o = createOrder(u1);
        mvc.perform(get("/api/orders/{id}", o.getId())
                .with(user(u2)))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}

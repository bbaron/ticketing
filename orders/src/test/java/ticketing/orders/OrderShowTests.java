package ticketing.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.oauth.JwtTestUtils;
import ticketing.common.test.MockMvcSetup;
import ticketing.messaging.types.OrderStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcSetup
@MockBean(AbstractMongoEventListener.class)
class OrderShowTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ObjectMapper objectMapper;
    final String u1 = ObjectId.get().toHexString();
    final String u2 = ObjectId.get().toHexString();
    final Jwt jwt1 = JwtTestUtils.createTestToken("asdf1@asdf.com", u1);
    final Jwt jwt2 = JwtTestUtils.createTestToken("asdf2@asdf.com", u2);

    private Ticket buildTicket() {
        var ticket = Ticket.of(null, "concert", 20);
        return ticketRepository.save(ticket);
    }

    private OrderResponse createOrder(Jwt jwt) throws Exception {
        var ticket = buildTicket();
        var content = """
                {"ticketId": "%s"}
                """.formatted(ticket.getId());
        var json = mvc.perform(post("/api/orders")
                .with(jwt().jwt(jwt))
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
        createOrder(jwt1);
        var o0 = createOrder(jwt2);
        var o1 = createOrder(jwt2);
        mvc.perform(get("/api/orders")
                .with(jwt().jwt(jwt2)))
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
        var o = createOrder(jwt1);
        mvc.perform(get("/api/orders/{id}", o.getId())
                .with(jwt().jwt(jwt1)))
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
        var o = createOrder(jwt1);
        mvc.perform(delete("/api/orders/{id}", o.getId())
                .with(jwt().jwt(jwt1)))
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
        var o = createOrder(jwt1);
        mvc.perform(get("/api/orders/{id}", o.getId())
                .with(jwt().jwt(jwt2)))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}

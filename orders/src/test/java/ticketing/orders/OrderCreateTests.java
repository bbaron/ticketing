package ticketing.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.test.MockMvcSetup;
import ticketing.messaging.test.MessageIO;
import ticketing.messaging.test.TestMessagingConfiguration;
import ticketing.messaging.types.OrderStatus;
import ticketing.orders.messaging.publishers.OrderCreatedMessage;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@MockMvcSetup
@Import(TestMessagingConfiguration.class)
class OrderCreateTests {
    final MockMvc mvc;
    final TicketRepository ticketRepository;
    final OrderRepository orderRepository;
    final ObjectMapper objectMapper;
    final MessageIO messageIO;


    @Autowired
    OrderCreateTests(MockMvc mvc, TicketRepository ticketRepository, OrderRepository orderRepository,
                     ObjectMapper objectMapper, MessageIO messageIO) {
        this.mvc = mvc;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.messageIO = messageIO;
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
        assertTrue(messageIO.neverReceived(0, "orderCreated"));
    }

    @Test
    @DisplayName("errors if ticket not found")
    @WithMockUser
    void errors_if_ticket_not_found() throws Exception {
        var ticketId = ObjectId.get()
                               .toHexString();
        var content = """
                {"ticketId": "%s"}
                """.formatted(ticketId);
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
           .andDo(print())
           .andExpect(status().isNotFound());
        assertTrue(messageIO.neverReceived(0, "orderCreated"));
    }

    @Test
    @DisplayName("errors if ticket is reserved")
    @WithMockUser
    void errors_if_ticket_is_reserved() throws Exception {
        var ticket = ticketRepository.save(Ticket.of(null, "concert", 20));
        var ticketId = ticket.getId();
        var order = Order.of("user", OrderStatus.Created, Instant.now(), ticket);
        orderRepository.save(order);
        // pop the order created message that was just emitted.
        messageIO.output.receive(0, "orderCreated");

        var content = """
                {"ticketId": "%s"}
                """.formatted(ticketId);
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
           .andDo(print())
           .andExpect(status().isBadRequest());
        assertTrue(messageIO.neverReceived(0, "orderCreated"));
    }


    @Test
    @DisplayName("reserves a ticket")
    @WithMockUser
    void reserves_a_ticket() throws Exception {
        var ticket = ticketRepository.save(Ticket.of(null, "concert", 20));

        var content = """
                {"ticketId": "%s"}
                """.formatted(ticket.getId());
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
           .andDo(print())
           .andExpect(status().isCreated());

        var message = messageIO.output.receive(5, "orderCreated");
        var payload = objectMapper.readValue(message.getPayload(), OrderCreatedMessage.class);
        assertEquals(ticket.getId(), payload.getTicket().getId());
    }

}

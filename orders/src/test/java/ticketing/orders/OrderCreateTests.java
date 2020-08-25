package ticketing.orders;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.events.Messenger;
import ticketing.common.events.types.OrderStatus;
import ticketing.common.test.MockMvcSetup;
import ticketing.orders.Order;
import ticketing.orders.OrderRepository;
import ticketing.orders.Ticket;
import ticketing.orders.TicketRepository;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
class OrderCreateTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    OrderRepository orderRepository;
    @MockBean
    Messenger messenger;

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
    }

    @Test
    @DisplayName("errors if ticket is reserved")
    @WithMockUser
    void errors_if_ticket_is_reserved() throws Exception {
        var ticket = new Ticket("concert", 20);
        ticket = ticketRepository.save(ticket);
        var ticketId = ticket.getId();
        var order = new Order("user", OrderStatus.Created, new Date(), ticket);
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
        var ticket = new Ticket("concert", 20);
        ticket = ticketRepository.save(ticket);

        var content = """
                {"ticketId": "%s"}
                """.formatted(ticket.getId());
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(messenger).convertAndSend(anyString(), anyString(), anyString());
    }

}

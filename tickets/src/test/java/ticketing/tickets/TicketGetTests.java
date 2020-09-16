package ticketing.tickets;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.test.MockMvcSetup;
import ticketing.tickets.messaging.publishers.TicketCreatedPublisher;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
public class TicketGetTests {
    final MockMvc mvc;
    final ObjectMapper objectMapper;
    final TicketRepository ticketRepository;
    @MockBean
    TicketCreatedPublisher ticketCreatedPublisher;

    @Autowired
    TicketGetTests(MockMvc mvc, ObjectMapper objectMapper,
                   TicketRepository ticketRepository) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
        this.ticketRepository = ticketRepository;
    }

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
    }

    @Test
    void unknown_id_returns_404() throws Exception {
        var id = ObjectId.get()
                         .toHexString();
        mvc.perform(get("/api/tickets/{id}", id))
           .andDo(print())
           .andExpect(status().isNotFound());
    }

    @Test
    void known_id_returns_ticket() throws Exception {
        var ticket = Ticket.of("title", 10, "user");
        var saved = ticketRepository.insert(ticket);
        mvc.perform(get("/api/tickets/{id}", saved.getId()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("id").value(saved.getId()))
           .andExpect(jsonPath("title").value(saved.getTitle()));
    }

    @Test
    void get_all_filters_out_reserved_tickets() throws Exception {
        var orderId = ObjectId.get()
                              .toHexString();
        var available = Ticket.of("available", 10, "user");
        var reserved = Ticket.of("reserved", 10, "user")
                             .withOrderId(orderId);
        available = ticketRepository.insert(available);
        ticketRepository.insert(reserved);
        System.out.println(ticketRepository.findByOrderIdIsNull());
        mvc.perform(get("/api/tickets"))
           .andDo(print())
           .andExpect(jsonPath("tickets").value(hasSize(1)))
           .andExpect(jsonPath("tickets[0].reserved").value(false))
           .andExpect(jsonPath("tickets[0].id").value(available.getId()));
    }

    @Test
    void foo_returns_404() throws Exception {
        mvc.perform(get("/foo"))
           .andDo(print())
           .andExpect(status().isNotFound());
    }
}

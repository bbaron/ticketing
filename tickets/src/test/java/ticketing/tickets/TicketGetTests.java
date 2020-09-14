package ticketing.tickets;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.test.MockMvcSetup;
import ticketing.tickets.messaging.publishers.TicketCreatedPublisher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketGetTests {
    final MockMvc mvc;
    final ObjectMapper objectMapper;
    final TicketRepository ticketRepository;
    @MockBean
    TicketCreatedPublisher ticketCreatedPublisher;

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
        var saved = ticketRepository.save(ticket);
        mvc.perform(get("/api/tickets/{id}", saved.getId()))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("id").value(saved.getId()))
           .andExpect(jsonPath("title").value(saved.getTitle()));
    }

    @Test
    void foo_returns_404() throws Exception {
        mvc.perform(get("/foo"))
           .andDo(print())
           .andExpect(status().isNotFound());
    }
}

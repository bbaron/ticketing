package ticketing.tickets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.test.MockMvcSetup;
import ticketing.messaging.test.MessageIO;
import ticketing.messaging.test.TestMessagingConfiguration;
import ticketing.tickets.messaging.publishers.TicketCreatedMessage;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@MockMvcSetup
@Import(TestMessagingConfiguration.class)
class TicketCreateTests {
    final MockMvc mvc;
    final MessageIO messageIO;
    final ObjectMapper objectMapper;

    @Autowired
    TicketCreateTests(MockMvc mvc, MessageIO messageIO, ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.messageIO = messageIO;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("has a route handler listening /api/tickets for post requests")
    void test1() throws Exception {
        mvc.perform(get("/api/tickets"))
                .andDo(print())
                .andExpect(status().is(not(404)));
        assertTrue(messageIO.neverReceived(0, "ticketCreated"));
    }

    @Test
    @DisplayName("only be access if the user is signed in")
    void test2() throws Exception {
        mvc.perform(post("/api/tickets/"))
                .andDo(print())
                .andExpect(status().is(403));
        assertTrue(messageIO.neverReceived(0, "ticketCreated"));
    }

    @Test
    @DisplayName("returns non 403 on sign in")
    @WithMockUser
    void test3() throws Exception {
        mvc.perform(post("/api/tickets"))
                .andDo(print())
                .andExpect(status().is(not(403)));
        assertTrue(messageIO.neverReceived(0, "ticketCreated"));
    }

    @Test
    @DisplayName("returns an error on invalid title")
    @WithMockUser
    void test4() throws Exception {
        String content = """
                {"title": "", "price": 10}
                """;
        mvc.perform(post("/api/tickets")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        content = """
                {"price": 10}
                """;
        mvc.perform(post("/api/tickets")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        assertTrue(messageIO.neverReceived(0, "ticketCreated"));
    }

    @Test
    @DisplayName("returns an error on invalid price")
    @WithMockUser
    void test5() throws Exception {
        String content = """
                {"title": "asdf", "price": null}
                """;
        mvc.perform(post("/api/tickets")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        content = """
                {"title": "asdf", "price": -10}
                """;
        mvc.perform(post("/api/tickets")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        assertTrue(messageIO.neverReceived(0, "ticketCreated"));
    }

    @Test
    @DisplayName("creates a ticket on valid inputs")
    @WithMockUser
    void test6() throws Exception {
        var title = "asdf";
        double price = 20;
        String content = """
                {"title": "%s", "price": %s}
                """.formatted(title, price);
        mvc.perform(post("/api/tickets")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is(201));

        var message = messageIO.output.receive(5,"ticketCreated");
        var payload = objectMapper.readValue(message.getPayload(), TicketCreatedMessage.class);
        assertEquals(title, payload.title);
    }

}

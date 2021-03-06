package ticketing.tickets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.tickets.messaging.publishers.TicketCreatedPublisher;
import ticketing.tickets.messaging.publishers.TicketUpdatedPublisher;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ticketing.common.oauth.JwtTestUtils.createTestToken;

@SpringBootTest
@AutoConfigureMockMvc
class TicketUpdateTests {
    @Autowired
    MockMvc mvc;
    @MockBean
    TicketUpdatedPublisher ticketUpdatedPublisher;
    @MockBean
    TicketCreatedPublisher ticketCreatedPublisher;
    @Autowired
    TicketRepository ticketRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    final Jwt jwt1 = createTestToken("asdf1@asdf.com");
    final Jwt jwt2 = createTestToken("asdf2@asdf.com");

    @Test
    @DisplayName("returns a 404 if the ticket id does not exist")
    void test1() throws Exception {
        String content = """
                {"title": "asdf", "price": 10}
                """;
        mvc.perform(put("/api/tickets/{id}", "asdf")
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(content))
           .andDo(print())
           .andExpect(status().is(404));
    }

    @Test
    @DisplayName("only be access if the user is signed in")
    void test2() throws Exception {
        mvc.perform(put("/api/tickets/{id}", "asdf"))
           .andDo(print())
           .andExpect(status().is(401));
    }

    @Test
    @DisplayName("returns a 403 if the user does not own the ticket")
    void test3() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
                          .andDo(print())
                          .andExpect(status().is(201))
                          .andReturn()
                          .getResponse()
                          .getContentAsString();
        var ticket = objectMapper.readValue(response, Map.class);

        mvc.perform(put("/api/tickets/{id}", ticket.get("id"))
                .with(jwt().jwt(jwt2))
                .contentType(APPLICATION_JSON)
                .content(request))
           .andDo(print())
           .andExpect(status().is(403));
    }

    @Test
    @DisplayName("returns an error on invalid title")
    void test4() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
                          .andDo(print())
                          .andExpect(status().is(201))
                          .andReturn()
                          .getResponse()
                          .getContentAsString();
        var ticket = objectMapper.readValue(response, Map.class);

        request = """
                {"price": 10}
                """;
        mvc.perform(put("/api/tickets/{id}", ticket.get("id"))
                .contentType(APPLICATION_JSON)
                .content(request))
           .andDo(print())
           .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("returns an error on invalid price")
    void test5() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
                          .andDo(print())
                          .andExpect(status().is(201))
                          .andReturn()
                          .getResponse()
                          .getContentAsString();
        var ticket = objectMapper.readValue(response, Map.class);

        request = """
                {"price": -10, "title": "asdf"}
                """;
        mvc.perform(put("/api/tickets/{id}", ticket.get("id"))
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
           .andDo(print())
           .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("updates the ticket on valid inputs and publishes event")
    void test6() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
                          .andDo(print())
                          .andExpect(status().is(201))
                          .andReturn()
                          .getResponse()
                          .getContentAsString();
        var ticket = objectMapper.readValue(response, Map.class);
        var title = "qwerty";
        request = """
                {"title": "%s", "price": 30}
                """.formatted(title);
        mvc.perform(put("/api/tickets/{id}", ticket.get("id"))
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
           .andDo(print())
           .andExpect(status().is(200));
        verify(ticketUpdatedPublisher).publish(any());
    }

    @Test
    @DisplayName("rejects updates if ticket is reserved")
    void test7() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
                          .andDo(print())
                          .andExpect(status().is(201))
                          .andReturn()
                          .getResponse()
                          .getContentAsString();
        var ticketId = objectMapper.readValue(response, Map.class)
                                   .get("id")
                                   .toString();
        var ticket = ticketRepository.findById(ticketId)
                                     .orElseThrow()
                                     .withOrderId(ObjectId.get()
                                                          .toHexString());
        ticketRepository.save(ticket);
        reset(ticketUpdatedPublisher);
        request = """
                {"title": "qwerty", "price": 200}
                """;
        mvc.perform(put("/api/tickets/{id}", ticketId)
                .with(jwt().jwt(jwt1))
                .contentType(APPLICATION_JSON)
                .content(request))
           .andDo(print())
           .andExpect(status().is(400));
        verify(ticketUpdatedPublisher, never()).publish(any());

    }
}

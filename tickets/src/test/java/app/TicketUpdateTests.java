package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.events.Messenger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketUpdateTests {
    @Autowired
    MockMvc mvc;
    @MockBean
    Messenger messenger;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("returns a 404 if the ticket id does not exist")
    @WithMockUser
    void test1() throws Exception {
        String content = """
                {"title": "asdf", "price": 10}
                """;
        mvc.perform(put("/api/tickets/{id}", "asdf")
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
                .andExpect(status().is(403));
    }

    @Test
    @DisplayName("returns a 403 if the user does not own the ticket")
    void test3() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(user("user1"))
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Ticket ticket = objectMapper.readValue(response, Ticket.class);

        mvc.perform(put("/api/tickets/{id}", ticket.getId())
                .with(user("user2"))
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    @DisplayName("returns an error on invalid title")
    @WithMockUser
    void test4() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(user("user1"))
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Ticket ticket = objectMapper.readValue(response, Ticket.class);

        request = """
                {"price": 10}
                """;
        mvc.perform(put("/api/tickets/{id}", ticket.getId())
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("returns an error on invalid price")
    @WithMockUser
    void test5() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .with(user("user1"))
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Ticket ticket = objectMapper.readValue(response, Ticket.class);

        request = """
                {"price": -10, "title": "asdf"}
                """;
        mvc.perform(put("/api/tickets/{id}", ticket.getId())
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("updates the ticket on valid inputs and publishes event")
    @WithMockUser
    void test6() throws Exception {
        String request = """
                {"title": "asdf", "price": 20}
                """;
        var response = mvc.perform(post("/api/tickets")
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Ticket ticket = objectMapper.readValue(response, Ticket.class);
        reset(messenger);
        var title = "qwerty";
        request = """
                {"title": "%s", "price": 30}
                """.formatted(title);
        mvc.perform(put("/api/tickets/{id}", ticket.getId())
                .contentType(APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().is(200));
        verify(messenger).convertAndSend(anyString(), anyString(),
                contains("""
                        "title":"%s"
                        """.formatted(title).strip()));
    }

}

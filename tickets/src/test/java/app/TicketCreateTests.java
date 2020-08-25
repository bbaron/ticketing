package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.events.Messenger;
import ticketing.common.test.MockMvcSetup;

import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
class TicketCreateTests {
    @Autowired
    MockMvc mvc;
    @MockBean
    Messenger messenger;

    @Test
    @DisplayName("has a route handler listening /api/tickets for post requests")
    void test1() throws Exception {
        mvc.perform(get("/api/tickets"))
                .andDo(print())
                .andExpect(status().is(not(404)));
    }

    @Test
    @DisplayName("only be access if the user is signed in")
    void test2() throws Exception {
        mvc.perform(post("/api/tickets"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    @DisplayName("returns non 403 on sign in")
    @WithMockUser
    void test3() throws Exception {
        mvc.perform(post("/api/tickets"))
                .andDo(print())
                .andExpect(status().is(not(403)));
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

        verify(messenger).convertAndSend(anyString(), anyString(),
                contains("\"title\":\"%s\"".formatted(title)));
    }

}

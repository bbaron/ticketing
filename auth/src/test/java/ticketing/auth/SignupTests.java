package ticketing.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.test.MockMvcSetup;

import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
class SignupTests {
    @Autowired
    MockMvc mvc;
    private static final String PATH = "/api/users/signup";
    private final TicketingProperties props = new TicketingProperties();

    @Test
    @DisplayName("has a route handler listening /api/users/signup for post requests")
    void test1() throws Exception {
        mvc.perform(post(PATH))
                .andDo(print())
                .andExpect(status().is(not(403)))
                .andExpect(status().is(not(404)))
                .andExpect(status().is(not(405)));
    }

    @Test
    @DisplayName("returns an error on invalid email")
    void test2() throws Exception {
        String content = """
                {"email": "asdf", "password": "asdf"}
                """;
        var response = mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("response = " + response);
        content = """
                {"email": null, "password": "asdf"}
                """;
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("returns an error on invalid password")
    void test3() throws Exception {
        String content = """
                {"email": "asdf@asdf.com", "password": "asd"}
                """;
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        content = """
                {"email": "asdf@asdf.com", "password": null}
                """;
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        content = """
                {"email": "asdf@asdf.com", "password": "012345678901234567890"}
                """;
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("creates user on valid input")
    void test4() throws Exception {
        var email = "asdf@asdf.com";
        String content = String.format("""
                {"email": "%s", "password": "asdf"}
                """, email);
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.currentUser.id").exists())
                .andExpect(jsonPath("$.currentUser.email").value(email))
                .andExpect(jsonPath("$.currentUser.iat").exists())
                .andDo(print());
    }
}

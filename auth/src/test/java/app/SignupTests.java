package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.test.MockMvcSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcSetup
class SignupTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    JwtUtils jwtUtils;
    private static final String PATH = "/api/users/signup";
    private final ObjectMapper objectMapper = new ObjectMapper();


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
                .andDo(print())
                .andExpect(status().is4xxClientError());

        content = """
                {"email": "asdf@asdf.com", "password": "012345678901234567890"}
                """;
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("creates user on valid input")
    void test4() throws Exception {
        MockHttpSession session = new MockHttpSession();
        var email = "asdf@asdf.com";
        String content = String.format("""
                {"email": "%s", "password": "asdf"}
                """, email);
        mvc.perform(post(PATH)
                .session(session)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print());
        var jwt = (String)session.getAttribute("jwt");
        assertNotNull(jwt);
        var currentUserResponse = jwtUtils.verifyJwt(jwt);
        System.out.println(currentUserResponse);
        assertNotNull(currentUserResponse.getCurrentUser());
        var currentUser = currentUserResponse.getCurrentUser();
        assertEquals("asdf@asdf.com", currentUser.getEmail());
    }
}

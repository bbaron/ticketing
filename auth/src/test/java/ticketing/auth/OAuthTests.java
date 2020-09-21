package ticketing.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.autoconfigure.TicketingProperties;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(properties = "ticketing.security.access-token-validity-seconds=2")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OAuthTests {
    final MockMvc mvc;
    final UserRepository userRepository;
    final TicketingProperties properties;
    final ObjectMapper objectMapper;
    static final String password = "asdf";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    private AppUser insertUser() {
        var email = "asdf@asdf.com";
        var user = AppUser.of(email, "{noop}" + password);
        return userRepository.insert(user);
    }

    @Test
    void access_token_is_obtained_using_valid_user_and_client() throws Exception {
        var user = insertUser();
        mvc.perform(post("/oauth/token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("grant_type", "password")
                .queryParam("username", user.getEmail())
                .queryParam("password", password)
                .queryParam("scope", "read"))
           .andDo(print())
           .andExpect(jsonPath("$.access_token").exists())
           .andExpect(jsonPath("$.refresh_token").exists())
           .andExpect(jsonPath("$.currentUser.id").value(user.getId()))
           .andExpect(jsonPath("$.currentUser.email").value(user.getEmail()))
           .andExpect(jsonPath("$.currentUser.iat").exists())
           .andExpect(status().isOk());
    }


    @Test
    void access_token_is_obtained_using_valid_refresh() throws Exception {
        var user = insertUser();
        var content = mvc.perform(post("/oauth/token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("grant_type", "password")
                .queryParam("username", user.getEmail())
                .queryParam("password", password)
                .queryParam("scope", "read"))
                         .andDo(print())
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
        var map = objectMapper.readValue(content, Map.class);
        var refreshToken = map.get("refresh_token")
                             .toString();
        mvc.perform(post("/oauth/token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("grant_type", "refresh_token")
                .queryParam("refresh_token", refreshToken)
                .queryParam("scope", "read"))
           .andDo(print())
           .andExpect(jsonPath("$.access_token").exists())
           .andExpect(jsonPath("$.refresh_token").exists())
           .andExpect(jsonPath("$.currentUser.id").value(user.getId()))
           .andExpect(jsonPath("$.currentUser.email").value(user.getEmail()))
           .andExpect(jsonPath("$.currentUser.iat").exists())
           .andExpect(status().isOk());
    }

    @Test
    void valid_token_checks() throws Exception {
        var user = insertUser();
        var content = mvc.perform(post("/oauth/token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("grant_type", "password")
                .queryParam("username", user.getEmail())
                .queryParam("password", password)
                .queryParam("scope", "read"))
                         .andDo(print())
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
        var map = objectMapper.readValue(content, Map.class);
        var accessToken = map.get("access_token")
                             .toString();
        mvc.perform(post("/oauth/check_token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("token", accessToken))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.currentUser.id").value(user.getId()))
           .andExpect(jsonPath("$.currentUser.email").value(user.getEmail()))
           .andExpect(jsonPath("$.currentUser.iat").exists());

    }

    @Test
    void expired_token_returns_400() throws Exception {
        var user = insertUser();
        var content = mvc.perform(post("/oauth/token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("grant_type", "password")
                .queryParam("username", user.getEmail())
                .queryParam("password", password)
                .queryParam("scope", "read"))
                         .andDo(print())
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
        var map = objectMapper.readValue(content, Map.class);
        var accessToken = map.get("access_token")
                             .toString();
        Thread.sleep(3000);
        mvc.perform(post("/oauth/check_token")
                .with(httpBasic(properties.security.clientId, "secret"))
                .queryParam("token", accessToken))
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error").value("invalid_token"))
           .andExpect(jsonPath("$.error_description").value("Token has expired"))
        ;

    }

    @Test
    void error_on_invalid_client_credentials() throws Exception {
        var user = insertUser();
        mvc.perform(post("/oauth/token")
                .with(httpBasic("foo", "bar"))
                .queryParam("grant_type", "password")
                .queryParam("username", user.getEmail())
                .queryParam("password", password)
                .queryParam("scope", "read"))
           .andDo(print())
           .andExpect(status().isUnauthorized());
    }

    @Test
    void error_on_invalid_user_credentials() throws Exception {
        var user = insertUser();
        mvc.perform(post("/oauth/token")
                .with(httpBasic("foo", "bar"))
                .queryParam("grant_type", "password")
                .queryParam("username", user.getEmail())
                .queryParam("password", "qwerty")
                .queryParam("scope", "read"))
           .andDo(print())
           .andExpect(status().isUnauthorized());
    }

}

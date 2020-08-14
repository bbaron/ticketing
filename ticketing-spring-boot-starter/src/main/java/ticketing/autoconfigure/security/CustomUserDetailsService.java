package ticketing.autoconfigure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;

public class CustomUserDetailsService extends PreAuthenticatedGrantedAuthoritiesUserDetailsService {
    private final ObjectMapper objectMapper;

    public CustomUserDetailsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected UserDetails createUserDetails(Authentication token, Collection<? extends GrantedAuthority> authorities) {
        var base64 = token.getName();
        var json =Base64.getDecoder().decode(base64);
        try {
            var map = objectMapper.readValue(json, Details.class);
            return new CustomUserDetails(map.getId(), map.getEmail(), authorities);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static class Details {
        String id, email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

package ticketing.tickets;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.security.HttpSecurityCustomizer;

import static org.springframework.http.HttpMethod.*;

@Component
public class SecurityConfig implements HttpSecurityCustomizer {


    @Override
    public void customize(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(POST, "/api/tickets").authenticated()
                .mvcMatchers(POST, "/").authenticated()
                .mvcMatchers(PUT, "/api/tickets/*").authenticated()
                .mvcMatchers(PUT, "/*").authenticated()
                .mvcMatchers(GET, "/**").permitAll()
        ;

    }
}

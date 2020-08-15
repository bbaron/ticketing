package app;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import ticketing.autoconfigure.security.HttpSecurityCustomizer;

import static org.springframework.http.HttpMethod.GET;

@Component
public class SecurityConfig implements HttpSecurityCustomizer {


    @Override
    public void customize(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(GET, "/api/tickets").permitAll()
                .antMatchers(GET, "/api/tickets/*").permitAll()
                .antMatchers("/api/tickets").authenticated()
                .antMatchers("/api/tickets/*").authenticated();

    }
}

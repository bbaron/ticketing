package ticketing.tickets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.oauth.ResourceServerConfigurerAdapter;

import static org.springframework.http.HttpMethod.*;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public ResourceServerConfig(TicketingProperties properties) {
        super(properties);
    }

    @Override
    public void customize(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers(POST, "/api/tickets")
            .authenticated()
            .mvcMatchers(POST, "/")
            .authenticated()
            .mvcMatchers(PUT, "/api/tickets/*")
            .authenticated()
            .mvcMatchers(PUT, "/*")
            .authenticated()
            .mvcMatchers(GET, "/**")
            .permitAll();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

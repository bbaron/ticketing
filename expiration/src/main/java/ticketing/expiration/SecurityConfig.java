package ticketing.expiration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.oauth.ResourceServerConfigurerAdapter;

@Component
public class SecurityConfig extends ResourceServerConfigurerAdapter {
    public SecurityConfig(TicketingProperties properties) {
        super(properties);
    }

    @Override
    public void customize(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
            .authenticated();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

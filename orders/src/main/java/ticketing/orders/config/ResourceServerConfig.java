package ticketing.orders.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.oauth.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public ResourceServerConfig(TicketingProperties properties) {
        super(properties);
    }

    @Override
    public void customize(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").authenticated();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

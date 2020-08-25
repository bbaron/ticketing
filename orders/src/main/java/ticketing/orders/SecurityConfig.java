package ticketing.orders;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.security.HttpSecurityCustomizer;

@Component
public class SecurityConfig implements HttpSecurityCustomizer {

    @Override
    public void customize(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").authenticated();
    }
}

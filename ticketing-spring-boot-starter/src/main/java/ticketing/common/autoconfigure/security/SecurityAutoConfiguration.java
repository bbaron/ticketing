package ticketing.common.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.jwt.JwtUtils;

import java.util.Optional;

//@EnableWebSecurity
//@Configuration
//@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
//@ConditionalOnProperty(prefix = "ticketing.security", value = "enabled", havingValue = "true", matchIfMissing = true)
//@Order(1)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
//    private final HttpSecurityCustomizer httpSecurityCustomizer;
//    private final TicketingProperties ticketingProperties;
//
//    public SecurityAutoConfiguration(TicketingProperties ticketingProperties,
//                                     Optional<HttpSecurityCustomizer> httpSecurityCustomizer) {
//        this.httpSecurityCustomizer = httpSecurityCustomizer.orElse(null);
//        this.ticketingProperties = ticketingProperties;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilter(requestHeaderAuthenticationFilter())
//                .csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
//
//        if (httpSecurityCustomizer != null) {
//            httpSecurityCustomizer.customize(http);
//        }
//    }
//
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
//        var filter = new RequestHeaderAuthenticationFilter();
//        filter.setPrincipalRequestHeader(ticketingProperties.getSecurity().getAuthHeaderName());
//        filter.setExceptionIfHeaderMissing(false);
//        filter.setContinueFilterChainOnUnsuccessfulAuthentication(true);
//        filter.setAuthenticationDetailsSource(customAuthenticationDetailsSource());
//        filter.setAuthenticationManager(authenticationManager());
//        return filter;
//    }
//
//    @Bean
//    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
//        var provider = new PreAuthenticatedAuthenticationProvider();
//        provider.setPreAuthenticatedUserDetailsService(customUserDetailsService());
//        return provider;
//    }
//
//    @Bean
//    public CustomAuthenticationDetailsSource customAuthenticationDetailsSource() {
//        return new CustomAuthenticationDetailsSource();
//    }
//
//    @Bean
//    public CustomUserDetailsService customUserDetailsService() {
//        return new CustomUserDetailsService(jwtUtils());
//    }
//
//    @Bean
//    public JwtUtils jwtUtils() {
//        return new JwtUtils(ticketingProperties);
//    }
}

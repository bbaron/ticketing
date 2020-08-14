package app;

import app.security.CustomAuthenticationManager;
import app.security.PreAuthFilter;
import common.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(NEVER).and()
                .addFilter(preAuthFilter())
                .authorizeRequests()
                .antMatchers(GET, "/api/tickets").permitAll()
                .antMatchers(GET, "/api/tickets/*").permitAll()
                .antMatchers("/api/tickets").authenticated()
                .antMatchers("/api/tickets/*").authenticated()
                .and()
                .csrf().disable();
    }

    @Bean
    public PreAuthFilter preAuthFilter() {
        var filter = new PreAuthFilter(jwtUtils);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        return new CustomAuthenticationManager(jwtUtils);
    }


}

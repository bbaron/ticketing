package ticketing.autoconfigure.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Callback interface that can be implemented to customize the
 * Spring Security {@link HttpSecurity}
 * configuration.
 *
 * Generally used to add authorizeRequest rules:
 *
 * <pre>
 *     http.authorizeRequests()
 *                 .antMatchers("/hello").permitAll()
 *                 .antMatchers("/**").authenticated()
 * </pre>
 */
public interface HttpSecurityCustomizer {
    /**
     * Customize the http security
     * @param http the instance to customize
     * @throws Exception propagates exceptions from HttpSecurity.
     */
    void customize(HttpSecurity http) throws Exception;
}

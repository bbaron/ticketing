package ticketing.autoconfigure.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> {
    @Override
    public CustomAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomAuthenticationDetails(context);
    }
}
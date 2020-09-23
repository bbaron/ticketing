package ticketing.common.autoconfigure.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

@Deprecated
public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> {
    @Override
    public CustomAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomAuthenticationDetails(context);
    }
}

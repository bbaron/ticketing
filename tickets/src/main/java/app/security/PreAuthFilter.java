package app.security;

import common.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class PreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PreAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;

    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtCookie(request).map(Cookie::getValue).orElse("NONE");
        logger.info("user {} extracted from jwt", jwt);
        return jwt;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return "N/A";
    }
}

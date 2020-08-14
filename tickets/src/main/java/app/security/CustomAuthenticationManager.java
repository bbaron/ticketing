package app.security;

import common.jwt.CurrentUserResponse;
import common.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class CustomAuthenticationManager implements AuthenticationManager {
    private final JwtUtils jwtUtils;
    private static final Collection<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    private Logger  logger = LoggerFactory.getLogger(getClass());

    public CustomAuthenticationManager(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = authentication.getName();
        CurrentUserResponse currentUser;
        if (!"NONE".equals(jwt)) {
            currentUser = jwtUtils.verifyJwt(jwt);
        } else {
            currentUser = CurrentUserResponse.NONE;
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentUser, "N/A", roles);
        logger.info("authenticated {}", token);
        return token;
    }
}

package ticketing.common.autoconfigure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import ticketing.common.exceptions.ForbiddenException;
import ticketing.common.jwt.JwtUtils;

import java.util.Collection;

public class CustomUserDetailsService extends PreAuthenticatedGrantedAuthoritiesUserDetailsService {
    private final JwtUtils jwtUtils;

    public CustomUserDetailsService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected UserDetails createUserDetails(Authentication token, Collection<? extends GrantedAuthority> authorities) {
        var user = jwtUtils.verifyJwt(token.getName()).getCurrentUser();
        if (user == null) {
            throw new ForbiddenException();
        }
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getIat(), authorities);
    }

}

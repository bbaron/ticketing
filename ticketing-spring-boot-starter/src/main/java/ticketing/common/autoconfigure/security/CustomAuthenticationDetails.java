package ticketing.common.autoconfigure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Deprecated
public class CustomAuthenticationDetails extends WebAuthenticationDetails implements GrantedAuthoritiesContainer {
    private static final List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public CustomAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        System.out.println(roles);
        return roles;
    }
}

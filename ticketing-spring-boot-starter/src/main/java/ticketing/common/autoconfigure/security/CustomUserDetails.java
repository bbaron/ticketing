package ticketing.common.autoconfigure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ticketing.common.jwt.CurrentUserResponse;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final String userId;
    private final Date iat;

    public CustomUserDetails(String id, String email, Date iat,
                             Collection<? extends GrantedAuthority> authorities) {
        this.userId = id;
        this.email = email;
        this.iat = iat;
        this.authorities = List.copyOf(authorities);
    }

    public String getEmail() {
        return email;
    }

    public Date getIat() {
        return iat;
    }

    private final String email;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(String userId, String email, Date iat, String role) {
        this.userId = userId;
        this.email = email;
        this.iat = iat;
        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "N/A";
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CurrentUserResponse toCurrentUserResponse() {
        return new CurrentUserResponse(userId, email, iat);
    }

}

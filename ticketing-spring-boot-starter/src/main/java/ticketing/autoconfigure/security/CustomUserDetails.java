package ticketing.autoconfigure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final String userId;

    public CustomUserDetails(String id, String email, Collection<? extends GrantedAuthority> authorities) {
        this.userId = id;
        this.email = email;
        this.authorities = List.copyOf(authorities);
    }

    public String getEmail() {
        return email;
    }

    private final String email;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(String userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    public CustomUserDetails(String userId, String email) {
        this(userId, email, "ROLE_USER");
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

}

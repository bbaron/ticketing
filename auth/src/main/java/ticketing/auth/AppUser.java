package ticketing.auth;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Document
public final class AppUser implements UserDetails {
    @Id
    private final String id;
    private final String email;
    private final String password;
    private static final List<GrantedAuthority> USER = List.of(new SimpleGrantedAuthority("USER"));

    @PersistenceConstructor
    public AppUser(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static AppUser of(String email, String password) {
        return new AppUser(null, email, password);
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return USER;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (o instanceof AppUser that) {
            return Objects.equals(this.email, that.email);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(email);
    }

    @Override
    public String toString() {
        return "AppUser{id='%s', email='%s', password='%s'}".formatted(id, email, password);
    }

    @SuppressWarnings("StringEquality")
    public AppUser withId(String id) {
        return this.id == id ? this : new AppUser(id, this.email, this.password);
    }
}

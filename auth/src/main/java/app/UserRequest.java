package app;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserRequest(
        @NotNull
        @Email
        @JsonProperty("email")
        String email,

        @NotBlank
        @Size(min = 4, max = 20)
        @JsonProperty("password")
        String password
) {

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(email, passwordEncoder.encode(password));
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        // used by validation
        return email;
    }

    @SuppressWarnings("unused")
    public String getPassword() {
        // used by validation
        return password;
    }
}

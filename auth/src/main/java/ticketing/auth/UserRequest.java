package ticketing.auth;


import lombok.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class UserRequest {
    @NotBlank
    @Email
    String email;

    @Size(min = 4, max = 20)
    @NotNull
    String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.of(email, passwordEncoder.encode(password));
    }

}

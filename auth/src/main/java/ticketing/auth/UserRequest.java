package ticketing.auth;


import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class UserRequest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @NotBlank
    @Email
    String email;

    @Size(min = 4, max = 20)
    @NotNull
    String password;

    public AppUser toUser(PasswordEncoder passwordEncoder) {
        var encodedPassword = passwordEncoder.encode(password);
        logger.info("encoded password: {}", encodedPassword);
        return AppUser.of(email, encodedPassword);
    }

}

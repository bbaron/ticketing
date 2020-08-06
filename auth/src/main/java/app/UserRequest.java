package app;


import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {
    @NotNull
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    public UserRequest() {

    }

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(email, passwordEncoder.encode(password));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

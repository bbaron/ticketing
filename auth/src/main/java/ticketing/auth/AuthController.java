package ticketing.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.common.autoconfigure.security.CurrentUser;
import ticketing.common.exceptions.RequestValidationException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {"/api/users", "/", ""})
@RequiredArgsConstructor
public class AuthController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/signup")
    @ResponseStatus(CREATED)
    public UserResponse signup(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors("email")) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                bindingResult.rejectValue("email", "duplicate-email", "Email in use");
            }
        }
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        var user = userRequest.toUser(passwordEncoder);
        user = userRepository.insert(user);
        var userResponse =  userResponse(user);
        logger.info("signed up " + userResponse);
        return userResponse;
    }

    @RequestMapping(path = "/signout")
    @ResponseStatus(OK)
    public void signout(HttpServletResponse response) {
        logger.info("Signing out current user, response: " + response);
    }

    private UserResponse userResponse(AppUser user) {
        return new UserResponse(new CurrentUser(user.getId(), user.getEmail(), Instant.now().toString()));
    }

}

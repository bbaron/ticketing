package ticketing.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.autoconfigure.security.CustomUserDetails;
import ticketing.common.exceptions.BadCredentialsException;
import ticketing.common.exceptions.RequestValidationException;
import ticketing.common.jwt.CurrentUserResponse;
import ticketing.common.jwt.JwtUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {"/api/users", "/", ""})
public class AuthController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TicketingProperties ticketingProperties;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          TicketingProperties ticketingProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.ticketingProperties = ticketingProperties;
    }

    @PostMapping(path = "/signup")
    @ResponseStatus(CREATED)
    public UserResponse signup(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult, HttpServletResponse response) {
        if ("blow@up.com".equals(userRequest.email())) {
            throw new IllegalStateException("What happens on this exception?");
        }
        if (!bindingResult.hasFieldErrors("email")) {
            if (userRepository.existsByEmail(userRequest.email())) {
                bindingResult.rejectValue("email", "duplicate-email", "Email in use");
            }
        }
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        var user = userRequest.toUser(passwordEncoder);
        user = userRepository.insert(user);
        sendAuthInfo(response, generateJwt(user));
        return user.toUserResponse();
    }

    @PostMapping(path = "/signin")
    @ResponseStatus(OK)
    public UserResponse signin(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult,
                               HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }

        var user = userRepository.findByEmail(userRequest.email());
        if (user == null) {
            throw new BadCredentialsException();
        }
        if (!passwordEncoder.matches(userRequest.password(), user.password())) {
            throw new BadCredentialsException();
        }
        sendAuthInfo(response, generateJwt(user));
        return user.toUserResponse();
    }

    @GetMapping(path = "/currentuser")
    public CurrentUserResponse currentUser(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return CurrentUserResponse.NONE;
        }
        return user.toCurrentUserResponse();
    }

    @RequestMapping(path = "/signout")
    @ResponseStatus(OK)
    public void signout(HttpServletResponse response) {
        logger.info("Signing out current user");
        sendAuthInfo(response, "SIGNED_OUT");
    }


    private String generateJwt(User user) {
        return jwtUtils.generateJwt(user.id(), user.email());
    }

    private void sendAuthInfo(HttpServletResponse response, String value) {
        response.addHeader(ticketingProperties.getSecurity().getAuthHeaderName(), value);
    }


}
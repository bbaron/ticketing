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
        var userResponse =  userResponse(user, response);
        logger.info("signed up " + userResponse);
        return userResponse;
    }

    @PostMapping(path = "/signin")
    @ResponseStatus(OK)
    public UserResponse signin(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult,
                               HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }

        var user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            throw new BadCredentialsException();
        }
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException();
        }
        var userResponse =  userResponse(user, response);
        logger.info("signed in " + userResponse);
        return userResponse;
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
        logger.info("Signing out current user, response: " + response);
//        response.addHeader(ticketingProperties.security.authHeaderName, "SIGNED_OUT");
    }

    private UserResponse userResponse(User user, HttpServletResponse response) {
        String jwt = jwtUtils.generateJwt(user.getId(), user.getEmail());
        response.addHeader(ticketingProperties.security.authHeaderName, jwt);
        var currentUserResponse = jwtUtils.verifyJwt(jwt);
        return new UserResponse(jwt, currentUserResponse.getCurrentUser());
    }

}

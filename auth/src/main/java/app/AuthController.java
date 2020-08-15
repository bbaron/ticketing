package app;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketing.autoconfigure.TicketingProperties;
import ticketing.exceptions.BadCredentialsException;
import ticketing.exceptions.RequestValidationException;
import ticketing.jwt.CurrentUserResponse;
import ticketing.jwt.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TicketingProperties ticketingProperties;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") JwtUtils jwtUtils,
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
            if (userRepository.existsByEmail(userRequest.email())) {
                bindingResult.rejectValue("email", "duplicate-email", "Email in use");
            }
        }
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        var user = userRequest.toUser(passwordEncoder);
        user = userRepository.insert(user);
        generateJwt(response, user);
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
        generateJwt(response, user);
        return user.toUserResponse();
    }

    @GetMapping(path = "/currentuser")
    public CurrentUserResponse currentUser(HttpServletRequest request) {
        return jwtUtils.getCurrentUser(request);
    }

    @RequestMapping(path = "/signout")
    @ResponseStatus(OK)
    public void signout(HttpServletRequest request, HttpServletResponse response) {
        jwtUtils.getJwtCookie(request).ifPresent(c -> {
            c.setMaxAge(0);
            response.addCookie(c);
        });
    }


    private void generateJwt(HttpServletResponse response, User user) {
        String jwt = jwtUtils.generateJwt(user.id(), user.email());
        response.addHeader(ticketingProperties.getSecurity().getAuthHeaderName(), jwt);
    }


}

package app;

import common.exceptions.BadCredentialsException;
import common.exceptions.RequestValidationException;
import common.jwt.CurrentUserResponse;
import common.jwt.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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

        var user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            throw new BadCredentialsException();
        }
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
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
        var jwt = jwtUtils.generateJwt(user.getId(), user.getEmail());
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }


}

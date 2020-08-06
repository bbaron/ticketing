package app;

import common.exceptions.BadCredentialsException;
import common.exceptions.RequestValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    public UserResponse signup(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult, HttpSession session) {
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
        generateJwt(session, user);
        return user.toUserResponse();
    }

    @PostMapping(path = "/signin")
    @ResponseStatus(OK)
    public UserResponse signin(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult, HttpSession session) {
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
        generateJwt(session, user);
        return user.toUserResponse();
    }

    @GetMapping(path = "/currentuser")
    public CurrentUserResponse currentUser(HttpSession session) {
        String jwt = (String) session.getAttribute("jwt");
        return jwtUtils.verifyJwt(jwt);
    }

    @RequestMapping(path = "/signout")
    @ResponseStatus(OK)
    public void signout(HttpSession session) {
        session.invalidate();
    }


    private void generateJwt(HttpSession session, User user) {
        var jwt = jwtUtils.generateJwt(user.getId(), user.getEmail());
        session.setAttribute("jwt", jwt);
    }


}

package ticketing.auth;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MongoUserDetailsService implements UserDetailsService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
                             .orElseThrow(() -> new UsernameNotFoundException(username));
        logger.info("user: {}", user);
        return user;
//        return new AppUser(ObjectId.get()
//                                   .toHexString(), username, "asdf");
    }

    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("password"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String rawPassword = "asdf";
        String encodedPassword = encoder.encode(rawPassword);
//        encodedPassword = "{bcrypt}$2a$10$nqP3Soax1GPJ7tvbbAxPpeHdwOOhYRixZ0JHO3Yl7P.ZUxria2.6W";
        System.out.println(encodedPassword);
        System.out.printf("raw '%s' matches encoded '%s'? %s%n", rawPassword, encodedPassword, encoder.matches(rawPassword, encodedPassword));

        rawPassword = "secret";
        encodedPassword = encoder.encode(rawPassword);
        System.out.printf("raw '%s' matches encoded '%s'? %s%n", rawPassword, encodedPassword, encoder.matches(rawPassword, encodedPassword));

    }
}

package app;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Key key;

    @Autowired
    public JwtUtils(Environment env) {
        this.key = Keys.hmacShaKeyFor(env.getRequiredProperty("jwt-key").getBytes());
    }

    public JwtUtils(String key) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
    }


    String generateJwt(String id, String email) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .signWith(key)
                .claim("id", id)
                .claim("email", email)
                .compact();
    }

    CurrentUserResponse verifyJwt(String jwt) {
        try {
            Jws<Claims> parsed = Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(jwt);
            var body = parsed.getBody();
            var iat = body.getIssuedAt();
            var id = body.get("id", String.class);
            var email = body.get("email", String.class);
            return new CurrentUserResponse(id, email, iat);

        } catch (Exception e) {
            log.info("verifyJwt exception", e);
            return CurrentUserResponse.NONE;
        }
    }

    public static void main(String[] args) {
        JwtUtils ju = new JwtUtils(UUID.randomUUID().toString());
        var jwt = ju.generateJwt("1234", "test@test.com");
        var cur = ju.verifyJwt(jwt);
        System.out.println(cur);
////        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        Key key = Keys.hmacShaKeyFor("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf".getBytes());
//        Key key2 = Keys.hmacShaKeyFor("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf1".getBytes());
//        String jws = Jwts.builder()
////                .setSubject("joe")
//                .setIssuedAt(new Date())
//                .signWith(key)
//                .claim("id", "w3;krapoijcoi3")
//                .claim("email", "test@test.com")
//                .compact();
//        System.out.println(jws);
//        Jws<Claims> parsed = Jwts.parserBuilder().setSigningKey(key2)
//                .build().parseClaimsJws(jws);
//        var body = parsed.getBody();
//        var iat = body.getIssuedAt();
//        var id = body.get("id");
//        var email = body.get("email");
//        System.out.printf("%s '%s' %s%n", (iat.getTime() / 1000), id, email);
    }
}

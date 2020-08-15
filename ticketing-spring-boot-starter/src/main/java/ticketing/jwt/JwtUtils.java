package ticketing.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class JwtUtils {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Key key;

    public JwtUtils(Environment env) {
        this.key = Keys.hmacShaKeyFor(env.getRequiredProperty("jwt-key").getBytes());
    }

    public JwtUtils(String key) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
    }


    public String generateJwt(String id, String email) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .signWith(key)
                .claim("id", id)
                .claim("email", email)
                .compact();
    }

    public CurrentUserResponse verifyJwt(String jwt) {
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

    public CurrentUserResponse getCurrentUser(HttpServletRequest request) {
        return getJwtCookie(request)
                .map(Cookie::getValue)
                .map(this::verifyJwt)
                .orElse(CurrentUserResponse.NONE);
    }

    public Optional<Cookie> getJwtCookie(HttpServletRequest request) {
        return Stream.of(request.getCookies())
                .filter(c -> "jwt".equals(c.getName()))
                .findFirst();
    }


}

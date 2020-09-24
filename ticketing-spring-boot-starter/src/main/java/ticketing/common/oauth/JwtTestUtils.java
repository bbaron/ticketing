package ticketing.common.oauth;

import org.bson.types.ObjectId;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

public final class JwtTestUtils {
    private JwtTestUtils() {
        throw new Error();
    }
    public static Jwt createTestToken(String email) {
        return createTestToken(email, ObjectId.get()
                                              .toHexString());
    }

    public static Jwt createTestToken(String email, String id) {
        var currentUser = new CurrentUser(id, email, Instant.now()
                                                            .toString());
        return Jwt.withTokenValue("token")
                  .header("alg", "HS256")
                  .claim("currentUser", currentUser)
                  .build();
    }

}

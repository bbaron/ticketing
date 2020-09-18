package ticketing.auth;

import lombok.Value;

import java.time.Instant;

@Value
public class CurrentUser {
    String id, email, iat;
}

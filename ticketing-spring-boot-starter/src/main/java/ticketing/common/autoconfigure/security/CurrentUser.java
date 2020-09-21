package ticketing.common.autoconfigure.security;

import lombok.Value;

@Value
public class CurrentUser {
    String id, email, iat;
}

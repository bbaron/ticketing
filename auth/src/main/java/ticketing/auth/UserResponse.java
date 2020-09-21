package ticketing.auth;

import lombok.Value;
import ticketing.common.autoconfigure.security.CurrentUser;

@Value
public class UserResponse {
    CurrentUser currentUser;
}

package ticketing.auth;

import lombok.Value;
import ticketing.common.jwt.CurrentUserResponse.CurrentUser;

@Value
public class UserResponse {
    String jwt;
    CurrentUser currentUser;
}

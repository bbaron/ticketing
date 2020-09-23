package ticketing.auth;

import lombok.Value;
import ticketing.common.oauth.CurrentUser;

@Value
public class UserResponse {
    CurrentUser currentUser;
}

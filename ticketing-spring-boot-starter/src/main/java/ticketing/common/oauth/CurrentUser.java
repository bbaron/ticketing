package ticketing.common.oauth;

import lombok.Value;

@Value
public class CurrentUser {
    String id, email, iat;
}

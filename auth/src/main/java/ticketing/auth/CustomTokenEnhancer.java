package ticketing.auth;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        var token = new DefaultOAuth2AccessToken(accessToken);
        Object principal = authentication.getPrincipal();
        Map<String, Object> info;
        if (principal instanceof AppUser user) {
            info = Map.of("currentUser", new CurrentUser(user.getId(), user.getEmail(), Instant.now().toString()));
        } else {
            info = Map.of("generatedInZone", ZoneId.systemDefault()
                                                   .toString());
        }
        token.setAdditionalInformation(info);
        return token;
    }
}

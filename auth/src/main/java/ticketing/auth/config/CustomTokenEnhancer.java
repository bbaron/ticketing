package ticketing.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import ticketing.auth.AppUser;
import ticketing.common.oauth.CurrentUser;

import java.time.Instant;
import java.util.Map;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        var token = new DefaultOAuth2AccessToken(accessToken);
        Object principal = authentication.getPrincipal();

        if (principal instanceof AppUser user) {
            var currentUser = new CurrentUser(user.getId(), user.getEmail(), Instant.now().toString());
            token.setAdditionalInformation(Map.of("currentUser", currentUser));
        }
        logger.info("custom enhancer exp: " + token.getExpiresIn());
        return token;
    }
}

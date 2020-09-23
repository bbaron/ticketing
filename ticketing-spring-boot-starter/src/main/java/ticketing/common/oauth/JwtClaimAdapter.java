package ticketing.common.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Map;

public class JwtClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Map.of());

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        var convertedClaims = delegate.convert(claims);
        logger.info("claims to convert: {}", convertedClaims);
        Object currentUserValue = convertedClaims.get("currentUser");
        if (currentUserValue instanceof Map<?,?> map) {
            String id = (String) map.get("id");
            String email = (String) map.get("email");
            String iat = (String) map.get("iat");
            var currentUser = new CurrentUser(id, email, iat);
            convertedClaims.put("currentUser", currentUser);
        }
        return convertedClaims;
    }
}

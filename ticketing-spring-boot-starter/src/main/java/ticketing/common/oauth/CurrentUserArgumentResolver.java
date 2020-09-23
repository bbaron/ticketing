package ticketing.common.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CurrentUser.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        Object resolved;
        if (principal instanceof Jwt jwt) {
            Object obj = jwt.getClaims()
                            .get("currentUser");
            if (obj == null) {
                resolved = null;
            } else if (obj instanceof CurrentUser cu) {
                resolved = cu;
            } else if (obj instanceof Map<?, ?> map) {
                String id = (String) map.get("id");
                String email = (String) map.get("email");
                String iat = (String) map.get("iat");
                resolved = new CurrentUser(id, email, iat);
            } else {
                resolved = null;
                logger.warn("Failed to convert currentUser claim {} {} to {}", obj, obj.getClass(), CurrentUser.class);
            }
        } else {
            resolved = null;
            logger.info("Principal {} {} is not a {}", principal, principal.getClass(), Jwt.class);
        }
        logger.info("Resolved {} to {}", parameter, resolved);
        return resolved;
    }
}

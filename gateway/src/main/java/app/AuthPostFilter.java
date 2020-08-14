package app;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthPostFilter extends ZuulFilter {
    private final Logger logger = LoggerFactory.getLogger("POST LOGGER");
    static final String AUTH_INFO = "X-Auth-Info";
    static final String AUTH_SIGNOUT = "X-Auth-Signout";

    public AuthPostFilter() {
        logger.info("Constructor for " + getClass());
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        logger.trace("AUTH POST FILTER RUN BEGIN " + getClass());
        var context = RequestContext.getCurrentContext();
        var session = context.getRequest().getSession(true);
        var authHeader = context.getOriginResponseHeaders()
                .stream()
                .filter(pair -> Objects.equals(AUTH_INFO, pair.first()))
                .map(Pair::second)
                .findFirst();

        authHeader.ifPresentOrElse(authInfo -> {
            context.remove(AUTH_INFO);
            context.remove(AUTH_INFO.toLowerCase());
            context.remove(AUTH_INFO.toUpperCase());
            logger.info("setting {} = {} on session {}", AUTH_INFO, authInfo, session.getId());
            session.setAttribute(AUTH_INFO, authInfo);
        }, () -> logger.info("No auth info in response"));

        logger.trace("AUTH POST FILTER RUN END " + getClass());

        return null;
    }
}

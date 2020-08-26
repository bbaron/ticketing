package ticketing.gateway;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class AuthPostFilter extends ZuulFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
                .filter(pair -> AUTH_INFO.equalsIgnoreCase(pair.first()))
                .map(Pair::second)
                .findFirst();

        authHeader.ifPresentOrElse(authInfo -> {
            logger.info("setting {} = {} on session {}", AUTH_INFO, authInfo, session.getId());
            session.setAttribute(AUTH_INFO, authInfo);
        }, () -> logger.info("No auth info in response"));

        logger.trace("AUTH POST FILTER RUN END " + getClass());

        return null;
    }
}

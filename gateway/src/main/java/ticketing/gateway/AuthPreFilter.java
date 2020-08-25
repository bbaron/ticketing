package ticketing.gateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthPreFilter extends ZuulFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AuthPreFilter() {
        logger.info("Constructor for " + getClass());
    }

    @Override
    public String filterType() {
        return "pre";
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
        logger.trace("AUTH PRE FILTER RUN BEGIN " + getClass());
        var context = RequestContext.getCurrentContext();
        var request = context.getRequest();
        var session = request.getSession(false);
        if (session != null) {
            Object authObj = session.getAttribute(AuthPostFilter.AUTH_INFO);
            if (authObj != null) {
                String authInfo = authObj.toString();
                if ("SIGNED_OUT".equalsIgnoreCase(authInfo)) {
                    logger.info("current user is logged out");
                } else {
                    logger.info("setting header {} = {} on request", AuthPostFilter.AUTH_INFO, authInfo);
                    context.addZuulRequestHeader(AuthPostFilter.AUTH_INFO, authInfo);
                }
            } else {
                logger.info("No auth info available");
            }
        } else {
            logger.info("No session available");
        }
        logger.trace("AUTH PRE FILTER RUN END " + getClass());
        return null;
    }
}

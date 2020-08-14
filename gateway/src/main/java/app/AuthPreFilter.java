package app;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static app.AuthPostFilter.AUTH_INFO;

@Component
public class AuthPreFilter extends ZuulFilter {
    private final Logger logger = LoggerFactory.getLogger("PRE LOGGER");

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
        System.out.println("AUTH PRE FILTER RUN BEGIN " + getClass());
        var context = RequestContext.getCurrentContext();
        var request = context.getRequest();
        var session = request.getSession(false);
        if (session != null) {
            var authInfo = session.getAttribute(AUTH_INFO);
            if (authInfo != null) {
                logger.info("setting header {} = {} on request", AUTH_INFO, authInfo);
                context.addZuulRequestHeader(AUTH_INFO, authInfo.toString());
            } else {
                logger.info("No auth info available");
            }
        } else {
            logger.info("No session available");
        }
        System.out.println("AUTH PRE FILTER RUN END " + getClass());
        return null;
    }
}
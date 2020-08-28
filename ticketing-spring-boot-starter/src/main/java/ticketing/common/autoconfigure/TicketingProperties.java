package ticketing.common.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@ConfigurationProperties(prefix = "ticketing")
public class TicketingProperties {
    public Security security = new Security();
    public Events events = new Events();
    public Orders orders = new Orders();

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "TicketingProperties{security=%s, events=%s, orders=%s}".formatted(security, events, orders);
    }

    public static class Security {
        public boolean enabled = true;
        public String authHeaderName = "x-auth-info";
        public String authSensitiveHeaderName = "x-auth-info-sensitive";
        public String jwtKey;

        public String getJwtKey() {
            return jwtKey;
        }

        public void setJwtKey(String jwtKey) {
            this.jwtKey = jwtKey;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAuthHeaderName() {
            return authHeaderName;
        }

        public void setAuthHeaderName(String authHeaderName) {
            this.authHeaderName = authHeaderName;
        }

        public String getAuthSensitiveHeaderName() {
            return authSensitiveHeaderName;
        }

        public void setAuthSensitiveHeaderName(String authSensitiveHeaderName) {
            this.authSensitiveHeaderName = authSensitiveHeaderName;
        }

        @Override
        public String toString() {
            return "Security{enabled=%s, authHeaderName='%s', authSensitiveHeaderName='%s'}"
                    .formatted(enabled, authHeaderName, authSensitiveHeaderName);
        }
    }

    public static class Events {
        public String exchange = "ticketing";

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        @Override
        public String toString() {
            return "Events{exchange='%s'}".formatted(exchange);
        }
    }

    public static class Orders {
        public long expirationWindowSeconds = SECONDS.convert(15, MINUTES);

        public long getExpirationWindowSeconds() {
            return expirationWindowSeconds;
        }

        public void setExpirationWindowSeconds(long expirationWindowSeconds) {
            this.expirationWindowSeconds = expirationWindowSeconds;
        }

        @Override
        public String toString() {
            return "Orders{expirationWindowSeconds=%d}".formatted(expirationWindowSeconds);
        }
    }

    @PostConstruct
    public void postConstruct() {
        Assert.hasLength(security.jwtKey, "jwt-key is a required property");
    }
}

package ticketing.common.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@ConfigurationProperties(prefix = "ticketing")
public class TicketingProperties {
    public Security security = new Security();
    public Events events = new Events();

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
    @Override
    public String toString() {
        return "TicketingProperties{security=%s}".formatted(security);
    }

    @PostConstruct
    public void postConstruct() {
        Assert.hasLength(security.jwtKey, "jwt-key is a required property");
    }
}

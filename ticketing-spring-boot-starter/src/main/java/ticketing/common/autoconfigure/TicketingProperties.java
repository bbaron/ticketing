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
    public Payments payments = new Payments();

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

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "TicketingProperties{security=%s, events=%s, orders=%s, payments=%s}".formatted(security, events, orders, payments);
    }

    public static class Security {
        public boolean enabled = true;
        public String authHeaderName = "x-auth-info";
        public String authSensitiveHeaderName = "x-auth-info-sensitive";
        public String jwtKey;
        public String clientId = "client";
        public String clientSecret = "{bcrypt}$2a$10$pdfbrvFYA.GRpPV4D0AfROayqhiDUs4aBLuoKLNRKf3VzI/l6Yks.";

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

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

    public static class Payments {
        public String stripeKey;

        public String getStripeKey() {
            return stripeKey;
        }

        public void setStripeKey(String stripeKey) {
            this.stripeKey = stripeKey;
        }

        @Override
        public String toString() {
            return "Payments{stripeKey='%s'}".formatted(stripeKey);
        }
    }

    @PostConstruct
    public void postConstruct() {
        Assert.hasLength(security.jwtKey, "ticketing.security.jwt-key is a required property");
        Assert.hasLength(payments.stripeKey, "ticketing.payments.stripe-key is a required property");

    }
}

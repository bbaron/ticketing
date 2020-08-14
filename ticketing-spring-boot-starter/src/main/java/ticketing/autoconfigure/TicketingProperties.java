package ticketing.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticketing")
public class TicketingProperties {
    private Security security = new Security();

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public static class Security {
        private boolean enabled = true;
        private String authHeaderName = "x-auth-info";
        private String authSensitiveHeaderName = "x-auth-info-sensitive";

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
            return String.format("Security{enabled=%s, authHeaderName='%s', authSensitiveHeaderName='%s'}",
                    enabled, authHeaderName, authSensitiveHeaderName);
        }
    }

    @Override
    public String toString() {
        return String.format("TicketingProperties{security=%s}", security);
    }
}

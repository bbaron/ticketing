package ticketing.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ticketing.autoconfigure.security.SecurityAutoConfiguration;
import ticketing.autoconfigure.web.WebAutoConfigure;

@Configuration
@Import({SecurityAutoConfiguration.class, WebAutoConfigure.class})
@EnableConfigurationProperties(TicketingProperties.class)
public class TicketingAutoConfiguration {
}

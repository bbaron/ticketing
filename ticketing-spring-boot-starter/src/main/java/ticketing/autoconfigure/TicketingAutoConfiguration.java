package ticketing.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ticketing.autoconfigure.events.EventsAutoConfiguration;
import ticketing.autoconfigure.json.JsonAutoConfiguration;
import ticketing.autoconfigure.security.SecurityAutoConfiguration;
import ticketing.autoconfigure.web.WebAutoConfiguration;

@Configuration
@Import({SecurityAutoConfiguration.class, WebAutoConfiguration.class, EventsAutoConfiguration.class, JsonAutoConfiguration.class})
@EnableConfigurationProperties(TicketingProperties.class)
public class TicketingAutoConfiguration {
}

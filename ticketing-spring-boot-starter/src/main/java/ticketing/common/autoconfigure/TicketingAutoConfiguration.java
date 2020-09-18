package ticketing.common.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ticketing.common.autoconfigure.events.EventsAutoConfiguration;
import ticketing.common.autoconfigure.json.JsonAutoConfiguration;
import ticketing.common.autoconfigure.security.SecurityAutoConfiguration;
import ticketing.common.autoconfigure.web.WebAutoConfiguration;

@Configuration
@Import({WebAutoConfiguration.class, EventsAutoConfiguration.class, JsonAutoConfiguration.class})
@EnableConfigurationProperties(TicketingProperties.class)
public class TicketingAutoConfiguration {
}

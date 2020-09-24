package ticketing.common.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebAutoConfiguration.class, ResourceServerAutoConfiguration.class})
@EnableConfigurationProperties(TicketingProperties.class)
public class TicketingAutoConfiguration {
}

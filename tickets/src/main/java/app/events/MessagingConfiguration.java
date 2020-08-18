package app.events;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.autoconfigure.TicketingProperties;


@Configuration
public class MessagingConfiguration {

    private final TicketingProperties properties;

    public MessagingConfiguration(TicketingProperties properties) {
        this.properties = properties;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(properties.events.exchange);
    }

}

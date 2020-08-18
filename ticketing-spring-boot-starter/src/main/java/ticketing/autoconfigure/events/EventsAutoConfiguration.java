package ticketing.autoconfigure.events;

import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.events.RabbitMessenger;

@Configuration
@ConditionalOnClass(RabbitOperations.class)
public class EventsAutoConfiguration {

    @Bean
    public RabbitMessenger rabbitMessenger(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") RabbitOperations rabbitOperations) {
        return new RabbitMessenger(rabbitOperations);
    }
}

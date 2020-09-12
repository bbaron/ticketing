package ticketing.messaging.test;

import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestMessagingConfiguration<T> extends TestChannelBinderConfiguration<T> {

    @Bean
    public MessageIO messageIO(InputDestination input, OutputDestination output) {
        return new MessageIO(input, output);

    }
}

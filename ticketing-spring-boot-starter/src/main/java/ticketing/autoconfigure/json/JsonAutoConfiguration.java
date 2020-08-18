package ticketing.autoconfigure.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.json.JacksonJsonOperations;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JsonAutoConfiguration {

    @Bean
    public JacksonJsonOperations jacksonJsonOperations(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") ObjectMapper objectMapper) {
        return new JacksonJsonOperations(objectMapper);
    }
}

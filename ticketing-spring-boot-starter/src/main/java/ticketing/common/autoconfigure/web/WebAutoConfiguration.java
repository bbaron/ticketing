package ticketing.common.autoconfigure.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.common.exceptions.ErrorHandlingAdvice;

@Configuration
public class WebAutoConfiguration {
    @Bean
    public ErrorHandlingAdvice errorHandlingAdvice() {
        return new ErrorHandlingAdvice();
    }
}

package ticketing.autoconfigure.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.exceptions.ErrorHandlingAdvice;

@Configuration
public class WebAutoConfiguration {
    @Bean
    public ErrorHandlingAdvice errorHandlingAdvice() {
        return new ErrorHandlingAdvice();
    }
}

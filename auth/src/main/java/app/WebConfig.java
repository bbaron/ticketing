package app;

import common.exceptions.ErrorHandlingAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public ErrorHandlingAdvice errorHandlingAdvice() {
        return new ErrorHandlingAdvice();
    }
}

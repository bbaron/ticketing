package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import ticketing.jwt.JwtUtils;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    public JwtUtils jwtUtils(Environment env) {
        return new JwtUtils(env);
    }
}

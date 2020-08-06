package app;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return  builder.routes()
                .route(r -> r.path("/api/tickets/**")
                .uri("lb://TICKETS")
                .id("tickets"))
                .route(r -> r.path("/api/orders/**")
                .uri("lb://ORDERS")
                .id("orders"))
                .route(r -> r.path("/api/users/**")
                .uri("lb://AUTH")
                .id("auth"))
                .build();
    }
}

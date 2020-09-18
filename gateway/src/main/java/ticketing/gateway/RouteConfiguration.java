package ticketing.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/users/**", "/oauth/**")
                        .uri("lb://AUTH")
                        .id("auth"))
                .route(r -> r.path("/api/tickets/**")
                        .uri("lb://TICKETS")
                        .id("tickets"))
                .route(r -> r.path("/api/orders/**")
                        .uri("lb://ORDERS")
                        .id("orders"))
                .route(r -> r.path("/api/expiration/**")
                        .uri("lb://EXPIRATION")
                        .id("expiration"))
                .route(r -> r.path("/api/payments/**")
                        .uri("lb://PAYMENTS")
                        .id("payments"))
                .route(r -> r.path("/**")
                        .uri("http://localhost:3000")
                        .id("client"))
                .build();
    }
}

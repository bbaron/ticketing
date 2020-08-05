package app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/orders")
@RefreshScope
public class OrderController {

    private final String applicationMessage;
    private final String orderMessage;
    private final DiscoveryClient discoveryClient;

    public OrderController(
            @Value("${message:No message}")
                    String applicationMessage,
            @Value("${order-message:No message}")
                    String orderMessage, DiscoveryClient discoveryClient) {
        this.applicationMessage = applicationMessage;
        this.orderMessage = orderMessage;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping
    List<Order> findAll() {
        return List.of();
    }

    @GetMapping("/messages")
    Map<String, String> getMessages() {
        return Map.of("message", applicationMessage, "order-message", orderMessage);
    }

    @GetMapping("/service-instances/{appName}")
    List<ServiceInstance> serviceInstancesByAppName(@PathVariable String appName) {
        return this.discoveryClient.getInstances(appName);
    }

}

package app;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final DiscoveryClient discoveryClient;
    private final Environment env;

    public AuthController(DiscoveryClient discoveryClient, Environment env) {
        this.discoveryClient = discoveryClient;
        this.env = env;
    }

    @GetMapping("/service-instances/{appName}")
    List<ServiceInstance> serviceInstancesByAppName(@PathVariable String appName) {
        return this.discoveryClient.getInstances(appName);
    }

    @GetMapping("/messages")
    Map<String, String> getMessages() {
        return Map.of("message", env.getProperty("message", "No message"),
                "auth-message", env.getProperty("auth-message", "No auth message"));
    }

}

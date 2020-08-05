package app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
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

    public OrderController(
            @Value("${message:No message}")
            String applicationMessage,
            @Value("${order-message:No message}")
            String orderMessage) {
        this.applicationMessage = applicationMessage;
        this.orderMessage = orderMessage;
    }

    @GetMapping
    List<Order> findAll() {
        return List.of();
    }

    @GetMapping("/messages")
    Map<String, String> getMessages() {
        return Map.of("message", applicationMessage, "order-message", orderMessage);
    }
}

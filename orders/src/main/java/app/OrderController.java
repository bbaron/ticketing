package app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/orders")
public class OrderController {

    @GetMapping
    List<Order> findAll() {
        return List.of();
    }
}

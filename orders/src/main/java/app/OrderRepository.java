package app;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByTicket(Ticket ticket);

    List<Order> findByUserId(String userId);
}

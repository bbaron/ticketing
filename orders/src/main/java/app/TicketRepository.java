package app;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findDistinctByIdAndVersion(String id, Long version);
}

package ticketing.auth;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AppUser, String> {
    boolean existsByEmail(String email);

    Optional<AppUser> findByEmail(String email);

}

package app;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record User(
        @Id
        String id,
        String email,
        String password
) {
    @SuppressWarnings("unused")
    public User withId(String id) {
        // used by spring data mongo
        return new User(id, email, password);
    }

    public User(String email, String password) {
        this(null, email, password);
    }

    public UserResponse toUserResponse() {
        return new UserResponse(id, email);
    }
}

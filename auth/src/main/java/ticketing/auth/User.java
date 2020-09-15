package ticketing.auth;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketing.common.jwt.CurrentUserResponse;

@Document
@Value
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
public class User {
    @Id
    @With
    String id;
    String email;
    String password;

    public static User of(String email, String password) {
        return new User(null, email, password);
    }

}

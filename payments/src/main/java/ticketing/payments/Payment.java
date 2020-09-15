package ticketing.payments;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import static java.util.Objects.requireNonNull;

@Document
@Value
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
public class Payment {
    @Id
            @With
    String id;
    String orderId;
    String stripeId;
    @Version
    Long version;

    public static Payment of(String orderId, String stripeId) {
        return new Payment(null, orderId, stripeId, null);
    }
}

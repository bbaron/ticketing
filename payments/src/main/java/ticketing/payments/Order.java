package ticketing.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketing.messaging.types.OrderStatus;

@Document
@Value
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Builder
public class Order {
    @Id
    @With
    String id;
    @With
    OrderStatus status;
    @With
    Integer price;
    @With
    String userId;
    @Version
    Long version;

    public static Order of(String id, OrderStatus status, int price, String userId) {
        return new Order(id, status, price, userId, null);
    }
}

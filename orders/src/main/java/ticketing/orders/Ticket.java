package ticketing.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Value
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Builder
public class Ticket {
    @Id
    @With
    String id;
    @With
    String title;
    @With
    Integer price;
    @Version
    Long version;
    @With
    String orderId;

    public static Ticket of(String id, String title, int price) {
        return new Ticket(id, title, price, null, null);
    }
}

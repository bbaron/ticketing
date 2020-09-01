package ticketing.payments;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import static java.util.Objects.requireNonNull;

@Document
public class Payment {
    @Id
    public String id;
    public String orderId;
    public String stripeId;
    @Version
    public Long version;

    public Payment() {
    }

    @PersistenceConstructor
    public Payment(@Nullable String id, String orderId, String stripeId, @Nullable Long version) {
        this.id = id;
        this.version = version;
        this.orderId = orderId;
        this.stripeId =stripeId;
    }

    public Payment(String orderId, String stripeId) {
        this.orderId = orderId;
        this.stripeId = stripeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Payment{id='%s', orderId='%s', stripeId='%s', version=%d}".formatted(id, orderId, stripeId, version);
    }
}

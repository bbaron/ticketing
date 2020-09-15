package ticketing.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class PaymentResponse {
    @JsonProperty("id")
    String id;

}

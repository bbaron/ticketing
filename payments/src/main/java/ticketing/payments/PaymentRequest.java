package ticketing.payments;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record PaymentRequest(
        @NotBlank
        @JsonProperty("token")
        String token,
        @NotBlank
        @JsonProperty("orderId")
        String orderId
) {
    public String getToken() {
        return token;
    }

    public String getOrderId() {
        return orderId;
    }

}

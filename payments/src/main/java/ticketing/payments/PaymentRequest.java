package ticketing.payments;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class PaymentRequest {
    @NotBlank
    String token;
    @NotBlank
    String orderId;

}

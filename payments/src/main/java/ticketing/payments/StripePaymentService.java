package ticketing.payments;

import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.common.exceptions.BadRequestException;

import java.util.Map;

@Component
public class StripePaymentService implements PaymentService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final RequestOptions requestOptions;

    @Autowired
    public StripePaymentService(TicketingProperties ticketingProperties) {
        this(ticketingProperties.payments.stripeKey);
    }

    public StripePaymentService(String stripeKey) {
        this(RequestOptions.builder()
                .setApiKey(stripeKey)
                .build());
    }

    public StripePaymentService(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
    }


    @Override
    public StripeCharge createCharge(PaymentRequest paymentRequest, Order order) {
        int amount = order.price * 100;
        Map<String, Object> params = Map.of(
                "amount", amount,
                "currency", "usd",
                "source", paymentRequest.token()
        );
        logger.info("creating stripe charge: " + params);
        try {
           Charge charge = Charge.create(params, requestOptions);
           return new StripeCharge(charge.getId());
        } catch (Exception e) {
            throw new BadRequestException("error making payment: " + e.getMessage(), e);
        }
    }
}

package ticketing.payments;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class StripePaymentServiceTests {
    static final String STRIPE_KEY = System.getenv("STRIPE_KEY");

    @BeforeAll
    static void beforeAll() {
        assumeTrue(STRIPE_KEY != null, "STRIPE_KEY must be defined in environment");
    }

    @Test
    void can_submit_charge() throws StripeException {
        int price = new Random().nextInt(100_000);
        var options = RequestOptions.builder()
                                    .setApiKey(STRIPE_KEY)
                                    .build();
        var service = new StripePaymentService(options);
        Order order = Order.builder()
                           .id(ObjectId.get()
                                       .toHexString())
                           .price(price)
                           .build();
        PaymentRequest paymentRequest = new PaymentRequest("tok_visa", order.getId());
        var stripeCharge = service.createCharge(paymentRequest, order);
        assertNotNull(stripeCharge.chargeId);
        var charge = Charge.retrieve(stripeCharge.chargeId, options);
        assertEquals(price * 100, charge.getAmount());
        assertEquals("usd", charge.getCurrency());
    }
}

package ticketing.payments;

public interface PaymentService {

    StripeCharge createCharge(PaymentRequest paymentRequest, Order order);
}

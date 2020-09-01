package ticketing.payments;

public class StripeCharge {
    public String chargeId;

    public StripeCharge() {
    }

    public StripeCharge(String chargeId) {
        this.chargeId = chargeId;
    }

    @Override
    public String toString() {
        return "StripeCharge{chargeId='%s'}".formatted(chargeId);
    }
}

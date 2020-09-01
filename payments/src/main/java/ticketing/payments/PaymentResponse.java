package ticketing.payments;

public class PaymentResponse {
    public String id;

    PaymentResponse() {
    }

    public PaymentResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PaymentResponse{id='%s'}".formatted(id);
    }
}

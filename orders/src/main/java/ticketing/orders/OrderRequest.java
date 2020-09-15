package ticketing.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import ticketing.common.validation.MongoId;

import javax.validation.constraints.NotNull;

@Value
public class OrderRequest {
    @NotNull
    @MongoId
    String ticketId;

    public OrderRequest(@JsonProperty("ticketId") String ticketId) {
        this.ticketId = ticketId;
    }

}

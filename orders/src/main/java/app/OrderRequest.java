package app;

import com.fasterxml.jackson.annotation.JsonProperty;
import ticketing.common.validation.MongoId;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record OrderRequest(
        @JsonProperty("ticketId")
        @NotNull
        @MongoId
        String ticketId
) {
    public String getTicketId() {
        return ticketId;
    }
}

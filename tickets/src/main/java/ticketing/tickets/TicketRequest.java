package ticketing.tickets;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class TicketRequest {
    @NotBlank
    String title;
    @Min(1)
    @NotNull
    Integer price;

}

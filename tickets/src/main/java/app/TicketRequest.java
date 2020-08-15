package app;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record TicketRequest(
        @NotBlank
        @JsonProperty("title")
        String title,
        @Min(1)
        @NotNull
        @JsonProperty("price")
        Double price
) {
    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

}

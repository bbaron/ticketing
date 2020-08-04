package app;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TicketRequest {

    @NotBlank
    private String title;
    @Min(1)
    @NotNull
    private Double price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}

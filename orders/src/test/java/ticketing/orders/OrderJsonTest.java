package ticketing.orders;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ticketing.messaging.types.OrderStatus;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderJsonTest {
    final JacksonTester<OrderRequest> orderRequestTester;
    final JacksonTester<OrderResponse> orderResponseTester;

    @Test
    void serialize_order_response() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var ticketId = ObjectId.get()
                               .toHexString();
        var price = 123;
        var userId = ObjectId.get()
                             .toHexString();
        var status = OrderStatus.Created;
        var expiresAt = Instant.now();
        var title = "title";
        Long version = 1L;

        var response = new OrderResponse(id, version, status, userId, expiresAt,
                new OrderResponse.TicketResponse(ticketId, title, price));
        var result = orderResponseTester.write(response);
        assertAll(() -> assertThat(result)
                        .extractingJsonPathStringValue("$.id")
                        .isEqualTo(id),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.userId")
                        .isEqualTo(userId),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.status")
                        .isEqualTo(status.name()),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.expiresAt")
                        .isEqualTo(expiresAt.toString()),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.ticket.id")
                        .isEqualTo(ticketId),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.ticket.title")
                        .isEqualTo(title),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.ticket.price")
                        .isEqualTo(price),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.version")
                        .isEqualTo(version.intValue())
        );
    }

    @Test
    void deserialize_order_request() throws IOException {
        var ticketId = ObjectId.get()
                              .toHexString();
        var expected = new OrderRequest(ticketId);
        String json = """
                { "ticketId":"%s"}""".formatted(ticketId);
        var actual = orderRequestTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
        assertThat(actual.getTicketId()).isEqualTo(ticketId);
    }

}

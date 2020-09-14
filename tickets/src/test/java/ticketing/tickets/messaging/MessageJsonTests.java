package ticketing.tickets.messaging;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ticketing.messaging.types.OrderStatus;
import ticketing.tickets.messaging.listeners.OrderCancelledMessage;
import ticketing.tickets.messaging.listeners.OrderCreatedMessage;
import ticketing.tickets.messaging.publishers.TicketCreatedMessage;
import ticketing.tickets.messaging.publishers.TicketUpdatedMessage;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MessageJsonTests {
    final JacksonTester<TicketCreatedMessage> ticketCreatedTester;
    final JacksonTester<TicketUpdatedMessage> ticketUpdatedTester;
    final JacksonTester<OrderCreatedMessage> orderCreatedTester;
    final JacksonTester<OrderCancelledMessage> orderCancelledTester;

    @Test
    void serialize_ticket_created_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var price = 123;
        var title = "concert";
        var userId = ObjectId.get()
                             .toHexString();
        Integer version = 1;
        var message = new TicketCreatedMessage(id, title, userId, price, version.longValue());
        var result = ticketCreatedTester.write(message);
        assertAll(() -> assertThat(result)
                        .extractingJsonPathStringValue("$.id")
                        .isEqualTo(id),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.title")
                        .isEqualTo(title),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.price")
                        .isEqualTo(price),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.version")
                        .isEqualTo(version),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.userId")
                        .isEqualTo(userId));
    }

    @Test
    void serialize_ticket_updated_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var price = 123;
        var title = "concert";
        var userId = ObjectId.get()
                             .toHexString();
        var orderId = ObjectId.get()
                              .toHexString();
        Integer version = 1;
        var message = new TicketUpdatedMessage(id, title, userId, price, version.longValue(), orderId);
        var result = ticketUpdatedTester.write(message);
        assertAll(() -> assertThat(result)
                        .extractingJsonPathStringValue("$.id")
                        .isEqualTo(id),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.title")
                        .isEqualTo(title),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.orderId")
                        .isEqualTo(orderId),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.price")
                        .isEqualTo(price),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.version")
                        .isEqualTo(version),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.userId")
                        .isEqualTo(userId));
    }

    @Test
    void deserialize_order_created_message() throws IOException {
        var orderId = ObjectId.get()
                              .toHexString();
        var ticketId = ObjectId.get()
                              .toHexString();
        var userId = ObjectId.get()
                              .toHexString();
        var expected = OrderCreatedMessage.of(orderId, ticketId);
        String json = """
                {
                "id":"%s",
                "userId":"%s",
                "status":"%s",
                "expiresAt":"%s",
                "version":%s,
                "ticket":{
                "id":"%s",
                "price":%s}}""".formatted(orderId, userId, OrderStatus.Created, Instant.now(),
                1L, ticketId, 20);
        var actual = orderCreatedTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void deserialize_order_cancelled_message() throws IOException {
        var orderId = ObjectId.get()
                              .toHexString();
        var ticketId = ObjectId.get()
                              .toHexString();
        var expected = OrderCancelledMessage.of(orderId, ticketId);
        String json = """
                {
                "id":"%s",
                "version":%s,
                "ticket":{
                "id":"%s"}}""".formatted(orderId, 2L, ticketId);
        var actual = orderCancelledTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

}

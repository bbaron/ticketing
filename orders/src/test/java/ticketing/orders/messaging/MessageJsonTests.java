package ticketing.orders.messaging;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ticketing.messaging.types.OrderStatus;
import ticketing.orders.messaging.listeners.ExpirationCompletedMessage;
import ticketing.orders.messaging.listeners.PaymentCreatedMessage;
import ticketing.orders.messaging.listeners.TicketCreatedMessage;
import ticketing.orders.messaging.listeners.TicketUpdatedMessage;
import ticketing.orders.messaging.publishers.OrderCancelledMessage;
import ticketing.orders.messaging.publishers.OrderCreatedMessage;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageJsonTests {
    final JacksonTester<ExpirationCompletedMessage> expirationCompletedTester;
    final JacksonTester<PaymentCreatedMessage> paymentCreatedTester;
    final JacksonTester<TicketCreatedMessage> ticketCreatedTester;
    final JacksonTester<TicketUpdatedMessage> ticketUpdatedTester;
    final JacksonTester<OrderCreatedMessage> orderCreatedTester;
    final JacksonTester<OrderCancelledMessage> orderCancelledTester;

    @Test
    void serialize_order_created_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var ticketId = ObjectId.get()
                               .toHexString();
        var price = 123;
        var userId = ObjectId.get()
                             .toHexString();
        var status = OrderStatus.Created;
        var expiresAt = Instant.now();
        Long version = 1L;
        var message = OrderCreatedMessage.of(id, userId, status, expiresAt, version, ticketId, price);
        var result = orderCreatedTester.write(message);
        System.out.println(result.getJson());
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
                        .extractingJsonPathNumberValue("$.ticket.price")
                        .isEqualTo(price),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.version")
                        .isEqualTo(version.intValue())
        );
    }

    @Test
    void serialize_order_cancelled_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var ticketId = ObjectId.get()
                               .toHexString();
        Long version = 1L;
        var message = OrderCancelledMessage.of(id, version, ticketId);
        var result = orderCancelledTester.write(message);
        assertAll(() -> assertThat(result)
                        .extractingJsonPathStringValue("$.id")
                        .isEqualTo(id),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.ticket.id")
                        .isEqualTo(ticketId),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.version")
                        .isEqualTo(version.intValue())
        );
    }

    @Test
    void deserialize_expiration_completed_message() throws IOException {
        var orderId = ObjectId.get()
                              .toHexString();
        var expected = new ExpirationCompletedMessage(orderId);
        String json = """
                { "orderId":"%s"}""".formatted(orderId);
        var actual = expirationCompletedTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void deserialize_payment_created_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var orderId = ObjectId.get()
                              .toHexString();
        var stripeId = ObjectId.get()
                               .toHexString();
        var expected = new PaymentCreatedMessage(id, orderId, stripeId);
        String json = """
                {"id":"%s", "orderId":"%s", "stripeId":"%s"}""".formatted(id, orderId, stripeId);
        var actual = paymentCreatedTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void deserialize_ticket_created_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var title = "title";
        int price = 40;
        Long version = 1L;
        var expected = new TicketCreatedMessage(id, title, price);
        String json = """
                {"id":"%s", "title":"%s", "price":"%s","version":"%s"}"""
                .formatted(id, title, price, version);
        var actual = ticketCreatedTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void deserialize_ticket_updated_message() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var userId = ObjectId.get()
                         .toHexString();
        var title = "title";
        int price = 40;
        long version = 1;
        var orderId = ObjectId.get()
                              .toHexString();
        var expected = new TicketUpdatedMessage(id, title, price, version, orderId);
        String json = """
                {"id":"%s", "title":"%s", "price":"%s", "version":"%s","orderId":"%s","userId":"%s"}"""
                .formatted(id, title, price, version, orderId, userId);
        var actual = ticketUpdatedTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

}

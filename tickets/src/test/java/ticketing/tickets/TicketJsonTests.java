package ticketing.tickets;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TicketJsonTests {
    final JacksonTester<TicketResponse> ticketResponseTester;
    final JacksonTester<TicketRequest> ticketRequestTester;
    final JacksonTester<TicketsResponse> ticketsResponseTester;

    @Test
    void serialize_ticket_response() throws IOException {
        var id = ObjectId.get()
                         .toHexString();
        var price = 123;
        var title = "concert";
        var ticketResponse = new TicketResponse(id, title, price, false);
        var result = ticketResponseTester.write(ticketResponse);
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
                        .extractingJsonPathBooleanValue("$.reserved")
                        .isFalse());
    }

    @Test
    void serialize_tickets_response() throws IOException {
        var id0 = ObjectId.get()
                          .toHexString();
        var price0 = 123;
        var title0 = "concert";
        var tr0 = new TicketResponse(id0, title0, price0, false);
        var id1 = ObjectId.get()
                          .toHexString();
        var price1 = 123;
        var title1 = "concert";
        var tr1 = new TicketResponse(id1, title1, price1, true);
        var ticketResponses = List.of(tr0, tr1);
        var ticketsResponse = new TicketsResponse(ticketResponses);
        var result = ticketsResponseTester.write(ticketsResponse);
        assertAll(
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.tickets[0].id")
                        .isEqualTo(id0),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.tickets[0].title")
                        .isEqualTo(title0),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.tickets[0].price")
                        .isEqualTo(price0),
                () -> assertThat(result)
                        .extractingJsonPathBooleanValue("$.tickets[0].reserved")
                        .isFalse(),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.tickets[1].id")
                        .isEqualTo(id1),
                () -> assertThat(result)
                        .extractingJsonPathStringValue("$.tickets[1].title")
                        .isEqualTo(title1),
                () -> assertThat(result)
                        .extractingJsonPathNumberValue("$.tickets[1].price")
                        .isEqualTo(price1),
                () -> assertThat(result)
                        .extractingJsonPathBooleanValue("$.tickets[1].reserved")
                        .isEqualTo(true)
        );
    }

    @Test
    void deserialize_ticket_request() throws IOException {
        var title = "Stanley Cup Finals Game 7";
        var price = 795;
        var expected = new TicketRequest(title, price);
        String json = """
                {"title":"%s","price":%s}""".formatted(title, price);
        var actual = ticketRequestTester.parseObject(json);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}

package ticketing.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ticketing.common.test.MockMvcSetup;
import ticketing.messaging.test.MessageIO;
import ticketing.messaging.test.TestMessagingConfiguration;
import ticketing.payments.messaging.publishers.PaymentCreatedMessage;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ticketing.messaging.types.OrderStatus.Cancelled;
import static ticketing.messaging.types.OrderStatus.Created;
import static ticketing.payments.Order.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@MockMvcSetup
@Import(TestMessagingConfiguration.class)
class CreatePaymentTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    OrderRepository orderRepository;
    @MockBean
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    MessageIO messageIO;
    @Autowired
    ObjectMapper objectMapper;
    final OrderBuilder orderBuilder = builder()
            .userId(ObjectId.get().toHexString())
            .price(20);

    @Test
    @DisplayName("return 404 when order does not exist")
    @WithMockUser
    void test1() throws Exception {
        var content = """
                {"token": "asdf",
                "orderId": "%s"}
                """.formatted(ObjectId.get().toHexString());
        mvc.perform(post("/api/payments")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isNotFound());
        assertTrue(messageIO.neverReceived(0, "paymentCreated"));

    }

    @Test
    @DisplayName("return 403 when purchasing an order that doesn't belong to the user")
    @WithMockUser
    void test2() throws Exception {
        Order order = orderBuilder.status(Created).build();
        order = orderRepository.save(order);
        var content = """
                {"token": "asdf",
                "orderId": "%s"}
                """.formatted(order.getId());
        mvc.perform(post("/api/payments")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isForbidden());
        assertTrue(messageIO.neverReceived(0, "paymentCreated"));

    }
    @Test
    @DisplayName("returns a 400 when the order is cancelled")
    void test3() throws Exception {
        Order order = orderBuilder.status(Cancelled).build();
        order = orderRepository.save(order);
        var content = """
                {"token": "asdf",
                "orderId": "%s"}
                """.formatted(order.getId());
        mvc.perform(post("/api/payments")
                .with(user(order.getUserId()))
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertTrue(messageIO.neverReceived(0, "paymentCreated"));
    }

    @Test
    @DisplayName("returns a 201 and submits charge on valid input")
    void test4() throws Exception {
        Order order = orderBuilder.status(Created).build();
        order = orderRepository.save(order);
        var request = new PaymentRequest("tok_visa", order.getId());
        StripeCharge stripeCharge = new StripeCharge(random(20));
        when(paymentService.createCharge(
                argThat(samePropertyValuesAs(request)),
                argThat(samePropertyValuesAs(order)))).thenReturn(stripeCharge);
        var content = """
                {"token": "tok_visa",
                "orderId": "%s"}
                """.formatted(order.getId());
        mvc.perform(post("/api/payments")
                .with(user(order.getUserId()))
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated());
        var payment = new Payment(order.getId(), stripeCharge.chargeId);
        assertTrue(paymentRepository.findOne(Example.of(payment)).isPresent());
        var message = messageIO.output.receive(5,"paymentCreated");
        var payload = objectMapper.readValue(message.getPayload(), PaymentCreatedMessage.class);
        assertEquals(stripeCharge.chargeId, payload.getStripeId());
    }
}

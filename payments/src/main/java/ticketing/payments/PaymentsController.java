package ticketing.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.common.exceptions.BadRequestException;
import ticketing.common.exceptions.ForbiddenException;
import ticketing.common.exceptions.NotFoundException;
import ticketing.common.exceptions.RequestValidationException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = {"/api/payments", "/", ""})
public class PaymentsController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public PaymentsController(OrderRepository orderRepository, PaymentRepository paymentRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }

    @PostMapping
    ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest paymentRequest,
                                           BindingResult bindingResult,
                                           Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        var order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(NotFoundException::new);
        if (!Objects.equals(order.getUserId(), principal.getName())) {
            throw new ForbiddenException();
        }
        if (order.getStatus().isCancelled()) {
            throw new BadRequestException("order is cancelled");
        }
        var stripeCharge = paymentService.createCharge(paymentRequest, order);
        var payment = new Payment( order.getId(), stripeCharge.chargeId);
        payment = paymentRepository.insert(payment);
        return status(CREATED).body(new PaymentResponse(payment.id));
    }

}

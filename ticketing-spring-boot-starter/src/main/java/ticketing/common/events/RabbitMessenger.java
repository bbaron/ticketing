package ticketing.common.events;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitOperations;

public class RabbitMessenger implements Messenger {
    private final RabbitOperations rabbitOperations;

    public RabbitMessenger(RabbitOperations rabbitOperations) {
        this.rabbitOperations = rabbitOperations;
    }

    @Override
    public void convertAndSend(String exchange, String routingKey, String message) throws AmqpException {
        rabbitOperations.convertAndSend(exchange, routingKey, message);
    }
}

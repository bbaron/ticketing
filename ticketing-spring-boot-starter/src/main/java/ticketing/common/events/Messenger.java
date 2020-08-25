package ticketing.common.events;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;

public interface Messenger {
    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a specific exchange
     * with a specific routing key.
     *
     * @param exchange the name of the exchange
     * @param routingKey the routing key
     * @param message a message to send
     * @throws AmqpException if there is a problem
     */
    void convertAndSend(String exchange, String routingKey, String message) throws AmqpException;

}

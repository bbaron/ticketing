package ticketing.orders.events;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.orders.events.listeners.expirationcompleted.ExpirationCompletedListener;
import ticketing.orders.events.listeners.paymentcreated.PaymentCreatedListener;
import ticketing.orders.events.listeners.ticketcreated.TicketCreatedListener;
import ticketing.orders.events.listeners.ticketupdated.TicketUpdatedListener;

import static org.springframework.amqp.core.BindingBuilder.bind;


@Configuration
public class MessagingConfiguration {

    static final String TICKET_CREATED_QUEUE = "orders.ticket-created";
    static final String TICKET_UPDATED_QUEUE = "orders.ticket-updated";
    static final String EXPIRATION_COMPLETED_QUEUE = "orders.expiration-completed";
    static final String PAYMENT_CREATED_QUEUE = "orders.payment-created";
    private final TicketingProperties properties;

    public MessagingConfiguration(TicketingProperties properties) {
        this.properties = properties;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(properties.events.exchange);
    }

    @Bean
    Queue ticketCreatedQueue() {
        return new Queue(TICKET_CREATED_QUEUE, true);
    }

    @Bean
    Queue ticketUpdatedQueue() {
        return new Queue(TICKET_UPDATED_QUEUE, true);
    }

    @Bean
    Queue expirationCompletedQueue() {
        return new Queue(EXPIRATION_COMPLETED_QUEUE, true);
    }

    @Bean
    Queue paymentCreatedQueue() {
        return new Queue(PAYMENT_CREATED_QUEUE, true);
    }

    @Bean
    Binding ticketCreatedBinding(TopicExchange exchange) {
        return bind(ticketCreatedQueue()).to(exchange).with("ticket.created.#");
    }

    @Bean
    Binding ticketUpdatedBinding(TopicExchange exchange) {
        return bind(ticketUpdatedQueue()).to(exchange).with("ticket.updated.#");
    }

    @Bean
    Binding expirationCompletedBinding(TopicExchange exchange) {
        return bind(expirationCompletedQueue()).to(exchange).with("expiration.completed.#");
    }

    @Bean
    Binding paymentCreatedBinding(TopicExchange exchange) {
        return bind(paymentCreatedQueue()).to(exchange).with("payment.created.#");
    }

    @Bean
    SimpleMessageListenerContainer ticketCreatedContainer(ConnectionFactory connectionFactory, TicketCreatedListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(TICKET_CREATED_QUEUE);
        container.setMessageListener(ticketCreatedAdapter(listener));
        return container;
    }

    @Bean
    MessageListenerAdapter ticketCreatedAdapter(TicketCreatedListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }

    @Bean
    SimpleMessageListenerContainer ticketUpdatedContainer(ConnectionFactory connectionFactory, TicketUpdatedListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(TICKET_UPDATED_QUEUE);
        container.setMessageListener(ticketUpdatedAdapter(listener));
        return container;
    }

    @Bean
    MessageListenerAdapter ticketUpdatedAdapter(TicketUpdatedListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }

    @Bean
    SimpleMessageListenerContainer expirationCompletedContainer(ConnectionFactory connectionFactory, ExpirationCompletedListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(EXPIRATION_COMPLETED_QUEUE);
        container.setMessageListener(expirationCompletedAdapter(listener));
        return container;
    }

    @Bean
    MessageListenerAdapter expirationCompletedAdapter(ExpirationCompletedListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }


    @Bean
    SimpleMessageListenerContainer paymentCreatedContainer(ConnectionFactory connectionFactory, PaymentCreatedListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PAYMENT_CREATED_QUEUE);
        container.setMessageListener(paymentCreatedAdapter(listener));
        return container;
    }

    @Bean
    MessageListenerAdapter paymentCreatedAdapter(PaymentCreatedListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }

}

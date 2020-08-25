package app.events;

import app.events.listeners.TicketCreatedListener;
import app.events.listeners.TicketUpdatedListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.autoconfigure.TicketingProperties;


@Configuration
public class MessagingConfiguration {

    static final String TICKET_CREATED_QUEUE = "orders.ticket-created";
    static final String TICKET_UPDATED_QUEUE = "orders.ticket-updated";
    private final TicketingProperties properties;

    public MessagingConfiguration(TicketingProperties properties) {
        this.properties = properties;
    }

    @Bean
    Queue ticketCreatedQueue() {
        return new Queue(TICKET_CREATED_QUEUE, false);
    }

    @Bean
    Queue ticketUpdatedQueue() {
        return new Queue(TICKET_UPDATED_QUEUE, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(properties.events.exchange);
    }

    @Bean
    Binding ticketCreatedBinding(TopicExchange exchange) {
        return BindingBuilder.bind(ticketCreatedQueue()).to(exchange).with("ticket.created.#");
    }

    @Bean
    Binding ticketUpdatedBinding(TopicExchange exchange) {
        return BindingBuilder.bind(ticketUpdatedQueue()).to(exchange).with("ticket.updated.#");
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


}

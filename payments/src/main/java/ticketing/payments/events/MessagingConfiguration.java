package ticketing.payments.events;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.payments.events.listeners.OrderCancelledListener;
import ticketing.payments.events.listeners.OrderCreatedListener;


@Configuration
public class MessagingConfiguration {
    static final String ORDER_CREATED_QUEUE = "payments.order-created";
    static final String ORDER_CANCELLED_QUEUE = "payments.order-cancelled";

    private final TicketingProperties properties;

    public MessagingConfiguration(TicketingProperties properties) {
        this.properties = properties;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(properties.events.exchange);
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, false);
    }

    @Bean
    Binding orderCreatedBinding(TopicExchange exchange) {
        return BindingBuilder.bind(orderCreatedQueue()).to(exchange).with("order.created.#");
    }

    @Bean
    SimpleMessageListenerContainer orderCreatedContainer(ConnectionFactory connectionFactory,
                                                         OrderCreatedListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ORDER_CREATED_QUEUE);
        container.setMessageListener(orderCreatedAdapter(listener));
        return container;
    }

    @Bean
    MessageListenerAdapter orderCreatedAdapter(OrderCreatedListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }

    @Bean
    Binding orderCancelledBinding(TopicExchange exchange) {
        return BindingBuilder.bind(orderCancelledQueue()).to(exchange).with("order.cancelled.#");
    }

    @Bean
    Queue orderCancelledQueue() {
        return new Queue(ORDER_CANCELLED_QUEUE, false);
    }

    @Bean
    SimpleMessageListenerContainer orderCancelledContainer(ConnectionFactory connectionFactory,
                                                           OrderCancelledListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ORDER_CANCELLED_QUEUE);
        container.setMessageListener(orderCancelledAdapter(listener));
        return container;
    }

    @Bean
    MessageListenerAdapter orderCancelledAdapter(OrderCancelledListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }


}

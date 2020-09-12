package ticketing.expiration.events;

import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;


//@Configuration
@EnableAsync
@Deprecated
public class MessagingConfiguration extends AsyncConfigurerSupport {
//
//    static final String ORDER_CREATED_QUEUE = "expiration.order-created";
//    private final TicketingProperties properties;
//
//    public MessagingConfiguration(TicketingProperties properties) {
//        this.properties = properties;
//    }
//
//    @Bean
//    Queue orderCreatedQueue() {
//        return new Queue(ORDER_CREATED_QUEUE, false);
//    }
//
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(properties.events.exchange);
//    }
//
//    @Bean
//    Binding orderCreatedBinding(TopicExchange exchange) {
//        return BindingBuilder.bind(orderCreatedQueue())
//                             .to(exchange)
//                             .with("order.created.#");
//    }
//
//    @Bean
//    SimpleMessageListenerContainer orderCreatedContainer(ConnectionFactory connectionFactory, OrderCreatedListener listener) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(ORDER_CREATED_QUEUE);
//        container.setMessageListener(orderCreatedAdapter(listener));
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter orderCreatedAdapter(OrderCreatedListener listener) {
//        return new MessageListenerAdapter(listener, "receiveMessage");
//    }
//
}

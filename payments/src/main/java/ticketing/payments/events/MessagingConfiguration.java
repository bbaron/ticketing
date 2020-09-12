package ticketing.payments.events;

//@Configuration
@Deprecated
public class MessagingConfiguration {
//    static final String ORDER_CREATED_QUEUE = "payments.order-created";
//    static final String ORDER_CANCELLED_QUEUE = "payments.order-cancelled";
//
//    private final TicketingProperties properties;
//
//    public MessagingConfiguration(TicketingProperties properties) {
//        this.properties = properties;
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(properties.events.exchange);
//    }
//
//    @Bean
//    Queue orderCreatedQueue() {
//        return new Queue(ORDER_CREATED_QUEUE, false);
//    }
//
//    @Bean
//    Binding orderCreatedBinding(TopicExchange exchange) {
//        return BindingBuilder.bind(orderCreatedQueue()).to(exchange).with("order.created.#");
//    }
//
//    @Bean
//    SimpleMessageListenerContainer orderCreatedContainer(ConnectionFactory connectionFactory,
//                                                         OrderCreatedListener listener) {
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
//    @Bean
//    Binding orderCancelledBinding(TopicExchange exchange) {
//        return BindingBuilder.bind(orderCancelledQueue()).to(exchange).with("order.cancelled.#");
//    }
//
//    @Bean
//    Queue orderCancelledQueue() {
//        return new Queue(ORDER_CANCELLED_QUEUE, false);
//    }
//
//    @Bean
//    SimpleMessageListenerContainer orderCancelledContainer(ConnectionFactory connectionFactory,
//                                                           OrderCancelledListener listener) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(ORDER_CANCELLED_QUEUE);
//        container.setMessageListener(orderCancelledAdapter(listener));
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter orderCancelledAdapter(OrderCancelledListener listener) {
//        return new MessageListenerAdapter(listener, "receiveMessage");
//    }
//
//
}

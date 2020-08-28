package ticketing.expiration.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import ticketing.common.autoconfigure.TicketingProperties;
import ticketing.expiration.events.listeners.OrderCreatedListener;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Configuration
@EnableAsync
public class MessagingConfiguration extends AsyncConfigurerSupport {

    static final String ORDER_CREATED_QUEUE = "expiration.order-created";
    public static final String REDIS_ORDER_EXPIRED_QUEUE = "order-expired";
    private final TicketingProperties properties;

    public MessagingConfiguration(TicketingProperties properties) {
        this.properties = properties;
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, false);
    }


    @Bean
    TopicExchange exchange() {
        return new TopicExchange(properties.events.exchange);
    }

    @Bean
    Binding orderCreatedBinding(TopicExchange exchange) {
        return BindingBuilder.bind(orderCreatedQueue()).to(exchange).with("order.created.#");
    }

    @Bean
    SimpleMessageListenerContainer orderCreatedContainer(ConnectionFactory connectionFactory, OrderCreatedListener listener) {
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

    @Override
    public Executor getAsyncExecutor() {
        SimpleAsyncTaskExecutor e = new SimpleAsyncTaskExecutor();
        e.setConcurrencyLimit(1);
        e.setThreadNamePrefix("order-expired-");
        return e;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        class AsyncExceptionLogger implements AsyncUncaughtExceptionHandler {
            protected final Logger logger = LoggerFactory.getLogger(getClass());

            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                logger.error("Async error in method: {}({})",
                        method.getName(), Arrays.stream(params).map(Object::toString)
                                .collect(Collectors.joining(", ")), ex);
            }
        }
        return new AsyncExceptionLogger();
    }

}

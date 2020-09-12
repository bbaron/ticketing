package ticketing.expiration.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Configuration
@EnableAsync
public class RedisConfiguration extends AsyncConfigurerSupport {
    public static final String REDIS_ORDER_EXPIRED_QUEUE = "order-expired";

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
                        method.getName(), Arrays.stream(params)
                                                .map(Object::toString)
                                                .collect(Collectors.joining(", ")), ex);
            }
        }
        return new AsyncExceptionLogger();
    }

}

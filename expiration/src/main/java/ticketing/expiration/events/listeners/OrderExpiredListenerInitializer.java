package ticketing.expiration.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderExpiredListenerInitializer implements ApplicationListener<ApplicationReadyEvent> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrderExpiredListener orderExpiredListener;

    public OrderExpiredListenerInitializer(OrderExpiredListener orderExpiredListener) {
        this.orderExpiredListener = orderExpiredListener;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("initializing {}", orderExpiredListener);
        orderExpiredListener.listen();
    }

}

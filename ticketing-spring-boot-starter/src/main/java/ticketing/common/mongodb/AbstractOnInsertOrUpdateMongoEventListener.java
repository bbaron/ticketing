package ticketing.common.mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;

import static ticketing.common.mongodb.InsertOrUpdateDecider.Decision;

public abstract class AbstractOnInsertOrUpdateMongoEventListener<E>
        extends AbstractMongoEventListener<E> implements MongoTicketingListener<E> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final InsertOrUpdateDecider insertOrUpdateDecider = new VersionInsertOrUpdateDecider();

    private boolean isInsert(MongoMappingEvent<?> event) {
        Decision decision = insertOrUpdateDecider.decide(event.getSource());
        return switch (decision) {
            case INSERT -> true;
            case UPDATE -> false;
            case WHO_KNOWS -> throw new IllegalStateException("cannot decide if %s is an insert or update".formatted(event));
        };
    }

    @Override
    public final void onBeforeSave(BeforeSaveEvent<E> event) {
        if (isInsert(event)) onBeforeInsert(new BeforeInsertEvent<>(event));
        else onBeforeUpdate(new BeforeUpdateEvent<>(event));
    }

    @Override
    public final void onAfterSave(AfterSaveEvent<E> event) {
        if (isInsert(event)) onAfterInsert(new AfterInsertEvent<>(event));
        else onAfterUpdate(new AfterUpdateEvent<>(event));
    }

    @Override
    public void onBeforeInsert(BeforeInsertEvent<E> beforeInsertEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("onBeforeInsert: {}", beforeInsertEvent);
        }
    }

    @Override
    public void onAfterInsert(AfterInsertEvent<E> afterInsertEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("onAfterInsert: {}", afterInsertEvent);
        }
    }

    @Override
    public void onBeforeUpdate(BeforeUpdateEvent<E> beforeUpdateEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("onBeforeUpdate: {}", beforeUpdateEvent);
        }
    }

    @Override
    public void onAfterUpdate(AfterUpdateEvent<E> afterUpdateEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("onAfterUpdate: {}", afterUpdateEvent);
        }
    }
}

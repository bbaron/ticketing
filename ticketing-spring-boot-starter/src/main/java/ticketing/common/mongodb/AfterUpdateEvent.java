package ticketing.common.mongodb;

import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

import static java.util.Objects.requireNonNull;

public class AfterUpdateEvent<E> extends AfterSaveEvent<E> {
    public AfterUpdateEvent(AfterSaveEvent<E> e) {
        super(e.getSource(), requireNonNull(e.getDocument()), requireNonNull(e.getCollectionName()));
    }
}

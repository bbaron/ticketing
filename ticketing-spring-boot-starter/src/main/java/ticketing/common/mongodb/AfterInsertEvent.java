package ticketing.common.mongodb;

import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

import static java.util.Objects.requireNonNull;

public class AfterInsertEvent<E> extends AfterSaveEvent<E> {
    public AfterInsertEvent(AfterSaveEvent<E> e) {
        super(e.getSource(), requireNonNull(e.getDocument()), requireNonNull(e.getCollectionName()));
    }
}

package ticketing.common.mongodb;

import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import static java.util.Objects.requireNonNull;

public class BeforeUpdateEvent<E> extends BeforeSaveEvent<E> {
    public BeforeUpdateEvent(BeforeSaveEvent<E> e) {
        super(e.getSource(), requireNonNull(e.getDocument()), requireNonNull(e.getCollectionName()));
    }
}

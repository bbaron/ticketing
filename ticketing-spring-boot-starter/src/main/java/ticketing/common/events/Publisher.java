package ticketing.common.events;

@Deprecated
public interface Publisher<E extends Event> {
    Subject subject();

    void publish(E event);
}

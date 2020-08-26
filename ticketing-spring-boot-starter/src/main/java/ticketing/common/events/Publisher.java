package ticketing.common.events;

public interface Publisher<E extends Event> {
    Subject subject();

    void publish(E event);
}

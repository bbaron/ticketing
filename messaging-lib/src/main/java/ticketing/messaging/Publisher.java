package ticketing.messaging;

public interface Publisher<T> {
    void publish(T event);
}

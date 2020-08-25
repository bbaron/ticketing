package ticketing.common.events;

public class TicketingMessage<E> {
    private Subject subject;
    private E event;

    public TicketingMessage() {
    }

    public TicketingMessage(Subject subject, E event) {
        this.subject = subject;
        this.event = event;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public E getEvent() {
        return event;
    }

    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "TicketingEvent{subject=%s, event=%s}".formatted(subject, event);
    }
}

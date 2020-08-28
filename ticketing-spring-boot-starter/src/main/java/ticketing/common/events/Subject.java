package ticketing.common.events;

public enum Subject {
    TicketCreated("ticket.created"),
    TicketUpdated("ticket.updated"),

    OrderCreated("order.created"),
    OrderCancelled("order.cancelled"),

    ExpirationCompleted("expiration.completed"),
    ;

    public final String routingKey;

    Subject(String routingKey) {
        this.routingKey = routingKey;
    }

}

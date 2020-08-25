package app;

import ticketing.common.events.types.OrderStatus;

import java.util.Date;

@SuppressWarnings("unused")
public class OrderResponse {
    private final String id;
    private final Long version;
    private final OrderStatus status;
    private final String userId;
    private final Date expiresAt;
    private final TicketResponse ticket;

    public OrderResponse(String id, Long version, OrderStatus status, String userId, Date expiresAt, TicketResponse ticket) {
        this.id = id;
        this.version = version;
        this.status = status;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.ticket = ticket;
    }

    public OrderResponse(Order order) {
        this(order.getId(),
                order.getVersion(),
                order.getStatus(),
                order.getUserId(),
                order.getExpiration(),
                new TicketResponse(order.getTicket().getId(), order.getTicket().getTitle(), order.getTicket().getPrice()));
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public TicketResponse getTicket() {
        return ticket;
    }

    public static class TicketResponse {
        private final String id;
        private final String title;
        private final Integer price;

        public TicketResponse(String id, String title, Integer price) {
            this.id = id;
            this.title = title;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Integer getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "TicketResponse{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                ", expiresAt=" + expiresAt +
                ", ticket=" + ticket +
                '}';
    }
}

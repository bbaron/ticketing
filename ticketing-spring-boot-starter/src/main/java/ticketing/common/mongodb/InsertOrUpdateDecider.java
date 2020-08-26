package ticketing.common.mongodb;

public interface InsertOrUpdateDecider {
    enum Decision {
        INSERT, UPDATE, WHO_KNOWS
    }

    Decision decide(Object entity);
}

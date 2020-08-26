package ticketing.common.mongodb;

public interface MongoTicketingListener<E> {
    void onBeforeInsert(BeforeInsertEvent<E> beforeInsertEvent);

    void onAfterInsert(AfterInsertEvent<E> afterInsertEvent);

    void onBeforeUpdate(BeforeUpdateEvent<E> beforeUpdateEvent);

    void onAfterUpdate(AfterUpdateEvent<E> afterUpdateEvent);

}

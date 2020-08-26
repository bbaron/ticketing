package ticketing.common.mongodb;

import static ticketing.common.mongodb.InsertOrUpdateDecider.Decision.*;

public class VersionInsertOrUpdateDecider implements InsertOrUpdateDecider {
    private final String versionFieldName;
    private final long updateVersionsAreGreaterThan;

    public VersionInsertOrUpdateDecider(String versionFieldName, Long updateVersionsAreGreaterThan) {
        this.versionFieldName = versionFieldName;
        this.updateVersionsAreGreaterThan = updateVersionsAreGreaterThan;
    }

    public VersionInsertOrUpdateDecider() {
        this("version", 0L);
    }

    @Override
    public Decision decide(Object entity) {
        Class<?> cls = entity.getClass();
        for (var field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals(versionFieldName)) {
                try {
                    Long version = (Long) field.get(entity);
                    if (version == null) return INSERT;
                    return (version > updateVersionsAreGreaterThan) ? UPDATE : INSERT;
                } catch (IllegalAccessException | ClassCastException e) {
                    return WHO_KNOWS;
                }
            }
        }
        return WHO_KNOWS;
    }
}

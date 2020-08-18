package ticketing.json;

public interface JsonOperations {
    String writeValueAsString(Object value) throws JsonException;

    <T> T readValue(String content, Class<T> valueType) throws JsonException;
}

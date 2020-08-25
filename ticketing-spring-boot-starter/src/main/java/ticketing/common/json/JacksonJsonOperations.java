package ticketing.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonOperations implements JsonOperations {
    private final ObjectMapper objectMapper;

    public JacksonJsonOperations(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String writeValueAsString(Object value) throws JsonException {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to write value [%s] as string".formatted(value), e);
        }
    }

    @Override
    public <T> T readValue(String content, Class<T> valueType) throws JsonException {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to read value [%s] %s".formatted(content, valueType), e);
        }
    }
}

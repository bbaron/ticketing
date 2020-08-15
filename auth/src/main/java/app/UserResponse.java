package app;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
        @JsonProperty("id")
        String id,
        @JsonProperty("email")
        String email) {
}

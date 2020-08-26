package ticketing.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
        @JsonProperty("id")
        String id,
        @JsonProperty("email")
        String email,
        @JsonProperty("jwt")
        String jwt) {
}

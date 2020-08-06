package app;

public class UserResponse {
    private final String id;
    private final String email;

    public UserResponse(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}

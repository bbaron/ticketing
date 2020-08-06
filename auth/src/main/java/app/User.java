package app;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    @Id
    private String id;
    private String email, password;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserResponse toUserResponse() {
        return new UserResponse(id, email);
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

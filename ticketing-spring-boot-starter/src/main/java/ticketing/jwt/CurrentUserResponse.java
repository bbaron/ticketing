package ticketing.jwt;


import java.util.Date;

public class CurrentUserResponse {
    private final CurrentUser currentUser;

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public CurrentUserResponse(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public CurrentUserResponse(String id, String email, Date iat) {
        this(new CurrentUser(id, email, iat));
    }

    private CurrentUserResponse() {
        currentUser = null;
    }

    public static final CurrentUserResponse NONE = new CurrentUserResponse();

    public static class CurrentUser {
        private final String id, email;
        private final Date iat;

        CurrentUser(String id, String email, Date iat) {
            this.id = id;
            this.email = email;
            this.iat = iat;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public Date getIat() {
            return iat;
        }

        @Override
        public String toString() {
            return "CurrentUser{" +
                    "id='" + id + '\'' +
                    ", email='" + email + '\'' +
                    ", iat=" + iat +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CurrentUserResponse{" +
                "currentUser=" + currentUser +
                '}';
    }
}

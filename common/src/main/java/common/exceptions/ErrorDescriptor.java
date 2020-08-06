package common.exceptions;

public class ErrorDescriptor {
    private final String message;
    private final String field;

    public ErrorDescriptor(String message) {
        this(message, null);
    }

    public ErrorDescriptor(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        if (field != null) {
            return "ErrorDescriptor{message='%s', field='%s'}".formatted(message, field);
        }
        return "ErrorDescriptor{message='%s'}".formatted(message);

    }
}

package ticketing.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class ErrorDescriptors {
    private final List<ErrorDescriptor> errors;

    public ErrorDescriptors() {
        this(List.of());
    }
    public ErrorDescriptors(List<ErrorDescriptor> errors) {
        this.errors = new ArrayList<>(errors);
    }

    public List<ErrorDescriptor> getErrors() {
        return List.copyOf(errors);
    }

    public static ErrorDescriptors of(String message, String field) {
        return new ErrorDescriptors(List.of(new ErrorDescriptor(message, field)));
    }

    public static ErrorDescriptors of(String message) {
        return new ErrorDescriptors(List.of(new ErrorDescriptor(message)));
    }

    public ErrorDescriptors add(String message) {
        errors.add(new ErrorDescriptor(message));
        return this;
    }

    public ErrorDescriptors add(String message, String field) {
        errors.add(new ErrorDescriptor(message, field));
        return this;
    }

    @Override
    public String toString() {
        return "ErrorDescriptors{" +
                "errors=" + errors +
                '}';
    }

    public static class ErrorDescriptor {
        private final String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
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

}

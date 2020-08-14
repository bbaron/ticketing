package ticketing.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class BadRequestException extends CustomException {
    private final String field;

    public BadRequestException(String message) {
        super(BAD_REQUEST, message);
        field = null;
    }

    public BadRequestException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public BadRequestException(String message, String field) {
        super(BAD_REQUEST, message);
        this.field = field;
    }

    public BadRequestException(String message, Throwable cause, String field) {
        super(BAD_REQUEST, message, cause);
        this.field = field;
    }

    @Override
    public ErrorDescriptors serializeErrors() {
        return ErrorDescriptors.of(getMessage(), field);
    }
}

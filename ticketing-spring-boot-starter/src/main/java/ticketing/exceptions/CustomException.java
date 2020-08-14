package ticketing.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;

    protected CustomException(String message) {
        this(DEFAULT_STATUS, message);
    }

    protected CustomException(String message, Throwable cause) {
        this(DEFAULT_STATUS, message, cause);
    }

    protected CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    protected CustomException(int httpStatus, String message) {
        this(HttpStatus.valueOf(httpStatus), message);
    }

    protected CustomException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    protected CustomException(int httpStatus, String message, Throwable cause) {
        this(HttpStatus.valueOf(httpStatus), message, cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public abstract ErrorDescriptors serializeErrors();
}

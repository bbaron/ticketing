package ticketing.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
public class ForbiddenException extends CustomException {
    public ForbiddenException() {
        super(FORBIDDEN, "Request forbidden");
    }

    public ForbiddenException(Throwable cause) {
        super(FORBIDDEN, "Request forbidden", cause);
    }

    @Override
    public ErrorDescriptors serializeErrors() {
        return ErrorDescriptors.of(getMessage());
    }
}

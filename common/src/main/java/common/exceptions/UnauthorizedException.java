package common.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class UnauthorizedException extends CustomException {
    public UnauthorizedException() {
        super(UNAUTHORIZED, "Unauthorized");
    }

    public UnauthorizedException(Throwable cause) {
        super(UNAUTHORIZED, "Unauthorized", cause);
    }

    @Override
    public ErrorDescriptors serializeErrors() {
        return ErrorDescriptors.of(getMessage());
    }
}

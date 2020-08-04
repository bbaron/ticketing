package app;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super();
    }
}

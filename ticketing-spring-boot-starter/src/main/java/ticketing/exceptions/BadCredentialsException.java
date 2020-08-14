package ticketing.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class BadCredentialsException extends CustomException {
    public BadCredentialsException() {
        super(UNAUTHORIZED, "Bad credentials");
    }

    public BadCredentialsException(Throwable cause) {
        super(UNAUTHORIZED, "Bad credentials", cause);
    }

    @Override
    public ErrorDescriptors serializeErrors() {
        return ErrorDescriptors.of(getMessage());
    }
}

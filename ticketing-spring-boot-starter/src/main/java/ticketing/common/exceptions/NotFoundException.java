package ticketing.common.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class NotFoundException extends CustomException {
    public NotFoundException() {
        super(NOT_FOUND, "Resource not found");
    }

    @Override
    public ErrorDescriptors serializeErrors() {
        return ErrorDescriptors.of(getMessage());
    }
}

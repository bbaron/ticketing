package common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class RequestValidationException extends CustomException {
    private final BindingResult bindingResult;

    public RequestValidationException(BindingResult bindingResult) {
        super(HttpStatus.BAD_REQUEST, "request validation errors: " + bindingResult);
        this.bindingResult = bindingResult;
    }

    @Override
    public ErrorDescriptors serializeErrors() {
        ErrorDescriptors eds = new ErrorDescriptors();
        for (var e : bindingResult.getGlobalErrors()) {
            eds.add(e.getDefaultMessage());
        }
        for (var e : bindingResult.getFieldErrors()) {
            eds.add(e.getDefaultMessage(), e.getField());
        }
        return eds;
    }
}

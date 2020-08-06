package common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandlingAdvice extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    ResponseEntity<ErrorDescriptors> handleCustomException(CustomException ex) {
        HttpStatus status = ex.getHttpStatus();
        logger.warn("handling exception, status = {}", status);
        return new ResponseEntity<>(ex.serializeErrors(), status);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ErrorDescriptors> handleException(HttpServletRequest request, Exception ex) {
        HttpStatus status = getStatus(request);
        logger.error("handling exception, status = {}", status, ex);
        var error = ErrorDescriptors.of(ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

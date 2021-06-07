package ge.bog.conversion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdviceException {
    @ExceptionHandler
    public ResponseEntity<?> handleException(RuntimeException e) {
        return new ResponseEntity<>(new GeneralExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

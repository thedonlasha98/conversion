package ge.bog.conversion.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceException {
    @ExceptionHandler
    public ResponseEntity<?> handleException(HttpServletRequest req, Exception e) {
        log.error("Request: " + req.getRequestURL() + " raised " + e);
        log.error("Request: " + req.getRequestURL() + " raised " + e.getStackTrace()[0].toString());
        return new ResponseEntity<>(new GeneralExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

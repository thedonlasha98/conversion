package ge.bog.conversion.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static ge.bog.conversion.exception.ErrorMessage.GENERAL_ERROR;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceException {
    @ExceptionHandler
    public ResponseEntity<?> handleException(HttpServletRequest req, Exception e) {
        String errorMessage = e.toString();
        log.error("Request: " + req.getRequestURL() + " raised " + e.getStackTrace()[0].toString());

        if (e instanceof GeneralException) {
            errorMessage = ((GeneralException) e).getErrorMessage();
            log.error("Request: " + req.getRequestURL() + " raised " + errorMessage);

            return new ResponseEntity<>(new GeneralExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult result = ((MethodArgumentNotValidException) e).getBindingResult();
            ObjectError error = result.getAllErrors().get(0);
            errorMessage = error.getDefaultMessage();
            log.error("Request: " + req.getRequestURL() + " raised " + errorMessage);

            return new ResponseEntity<>(new GeneralExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
        } else {
            log.error("Request: " + req.getRequestURL() + " raised " + errorMessage);
            return new ResponseEntity<>(new GeneralExceptionResponse(GENERAL_ERROR.getDescription()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

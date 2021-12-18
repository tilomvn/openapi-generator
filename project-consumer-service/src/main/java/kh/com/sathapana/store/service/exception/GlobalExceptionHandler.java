package kh.com.sathapana.store.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import kh.com.sathapana.store.model.Error;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<Error> handleAllExceptions(Exception ex) {
        log.error("Got unexpected error {}", ex);
        return new ResponseEntity<>(
                new Error().code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).message(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public final ResponseEntity<Error> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new Error().code(HttpStatus.FORBIDDEN.getReasonPhrase()).message(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(StoreException.class)
    public final ResponseEntity<Error> handleStoreException(StoreException ex) {
        return new ResponseEntity<>(
                new Error().code(ex.getHttpStatus().getReasonPhrase()).message(ex.getMessage()),
                ex.getHttpStatus()
        );
    }
}

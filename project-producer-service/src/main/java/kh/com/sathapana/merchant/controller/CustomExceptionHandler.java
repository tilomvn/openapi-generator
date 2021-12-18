package kh.com.sathapana.merchant.controller;

import kh.com.sathapana.merchant.exception.DuplicateMerchantException;
import kh.com.sathapana.merchant.exception.MerchantException;
import kh.com.sathapana.merchant.exception.MerchantNotFoundException;
import kh.com.sathapana.merchant.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<Error> handleAllExceptions(Exception ex) {
        log.error("Got an expected error: {}", ex);
        return new ResponseEntity<>(
                new Error().code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).message(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MerchantException.class)
    public final ResponseEntity<Error> handleMerchantException(MerchantException ex) {
        return new ResponseEntity<>(
                new Error().code(ex.getHttpStatus().getReasonPhrase()).message(ex.getMessage()),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(MerchantNotFoundException.class)
    public final ResponseEntity<Error> handleMerchantNotFoundException(MerchantNotFoundException ex) {
        return new ResponseEntity<>(
                new Error().code(HttpStatus.NOT_FOUND.getReasonPhrase()).message(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateMerchantException.class)
    public final ResponseEntity<Error> handleDuplicateMerchantException(DuplicateMerchantException ex) {
        return new ResponseEntity<>(
                new Error().code(HttpStatus.CONFLICT.getReasonPhrase()).message(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }
}

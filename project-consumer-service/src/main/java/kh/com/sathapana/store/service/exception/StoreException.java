package kh.com.sathapana.store.service.exception;

import org.springframework.http.HttpStatus;

public class StoreException extends RuntimeException{
    private final HttpStatus httpStatus;
    public StoreException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

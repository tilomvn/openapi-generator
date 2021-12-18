package kh.com.sathapana.merchant.exception;

import org.springframework.http.HttpStatus;

public class MerchantException extends RuntimeException{
    private final HttpStatus httpStatus;
    public MerchantException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

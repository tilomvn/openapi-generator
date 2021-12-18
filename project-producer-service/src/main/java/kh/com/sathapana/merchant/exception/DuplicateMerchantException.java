package kh.com.sathapana.merchant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateMerchantException extends RuntimeException {
    public DuplicateMerchantException(String message) {
        super(message);
    }
}

package kh.com.sathapana.merchant.controller;

import kh.com.sathapana.merchant.model.*;
import kh.com.sathapana.merchant.api.MerchantServiceApi;
import kh.com.sathapana.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MerchantController implements MerchantServiceApi {

    private final MerchantService service;

    public MerchantController(MerchantService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<CashiersResponse> getCashiers(String userId, CashierRequest request) {
        log.debug("Get cashier list: {}", request);
        var response = service.getCashiers(userId, request);
        return ResponseEntity.ok(response);
    }
}

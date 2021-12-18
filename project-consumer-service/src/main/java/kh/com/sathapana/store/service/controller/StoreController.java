package kh.com.sathapana.store.service.controller;

import kh.com.sathapana.store.service.security.SecurityService;
import kh.com.sathapana.store.service.service.StoreService;
import kh.com.sathapana.store.api.StoreServiceApi;
import kh.com.sathapana.store.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class StoreController implements StoreServiceApi {

    private final SecurityService securityService;
    private final StoreService storeService;

    public StoreController(SecurityService securityService, StoreService storeService) {
        this.securityService = securityService;
        this.storeService = storeService;
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT', 'MANAGER') && @securityService(userId)")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT', 'MANAGER')")
    @Override
    public ResponseEntity<StoreDetailResponse> _getStore(String storeId, String authorization) {
        return ResponseEntity.ok(storeService.getStore(storeId, authorization));
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @Override
    public ResponseEntity<UserRegistrationResponse> _registerUser(String authorization, String userId, MultipartFile logo, UserDTO user) {
        return ResponseEntity.ok(storeService.registerUser(userId, logo, user));
    }
}

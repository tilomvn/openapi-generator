package kh.com.sathapana.store.service.security;

import kh.com.sathapana.store.service.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityService {

    private final StoreRepository storeRepository;
    public SecurityService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public boolean hasWritePermissionOnStore(String storeId, String userId) {
        return true;
    }
}

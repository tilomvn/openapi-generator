package kh.com.sathapana.store.service.service;

import kh.com.sathapana.merchant.api.MerchantServiceApi;
import kh.com.sathapana.merchant.model.CashierRequest;
import kh.com.sathapana.merchant.model.CashiersResponse;
import kh.com.sathapana.store.service.configuration.ReqAuthToken;
import kh.com.sathapana.store.service.exception.StoreException;
import kh.com.sathapana.store.model.*;
import kh.com.sathapana.store.service.utils.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class StoreService {

    private final MerchantServiceApi merchantServiceApi;

    @Transactional
    public StoreDetailResponse getStore(String storeId, String authorization) {
        var authToken = (ReqAuthToken)SecurityContextHolder.getContext().getAuthentication();
        var userId = String.valueOf(authToken.getPrincipal());
        var cashiers = getCashiers(userId, "store.getMerchantId()");
        return new StoreDetailResponse().success(true).message(Message.SUCCESS);
    }

    private CashiersResponse getCashiers(String userId, String merchantId) {
        try {
            return merchantServiceApi.getCashiers(userId, new CashierRequest().merchantId(merchantId));
        } catch (RestClientException e) {
            log.error("Got an error while getting cashieres from merchant {}", e);
            throw new StoreException(HttpStatus.INTERNAL_SERVER_ERROR, Message.INTERNAL_ERROR);
        }
    }

    @Transactional
    public UserRegistrationResponse registerUser(String userId, MultipartFile logo, UserDTO request) {
        log.info(request.getId());
        try {
            log.info(logo.getBytes().toString());
        } catch (Exception e) {
            log.info("File upload failed");
            e.printStackTrace();
        }
        return new UserRegistrationResponse().success(true).message(Message.SUCCESS).data(request);
    }

    private byte[] toBytes(MultipartFile multipartFile) {
        if (multipartFile != null) {
            try {
                return multipartFile.getBytes();
            } catch (IOException ex) {
                log.error("Read multipart file failed. {}", ex);
                throw new StoreException(HttpStatus.INTERNAL_SERVER_ERROR, Message.INTERNAL_ERROR);
            }
        }
        return null;
    }
}

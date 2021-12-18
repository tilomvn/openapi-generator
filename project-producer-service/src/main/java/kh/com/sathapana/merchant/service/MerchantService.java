package kh.com.sathapana.merchant.service;

import kh.com.sathapana.merchant.entity.*;
import kh.com.sathapana.merchant.entity.Merchant;
import kh.com.sathapana.merchant.exception.MerchantException;
import kh.com.sathapana.merchant.model.*;
import kh.com.sathapana.merchant.repository.MerchantRepository;
import kh.com.sathapana.merchant.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantService {

    private final UserRepository userRepository;
    private final Clock clock;
    private final MerchantRepository merchantRepository;

    public MerchantService(UserRepository userRepository, Clock clock, MerchantRepository merchantRepository) {
        this.userRepository = userRepository;
        this.clock = clock;
        this.merchantRepository = merchantRepository;
    }

    public CashiersResponse getCashiers(String userId, CashierRequest request) {
        var optional = userRepository.findById(userId);
        if (!optional.isPresent()) {
            log.error("User {} doesn't have permission to get merchant list", userId);
            throw new MerchantException(HttpStatus.FORBIDDEN, "You don't have permission to do this action");
        }
        var user = optional.get();
        var merchant = user.getMerchant();
        if (user.getRole().compareTo(Role.ADMIN) == 0) {
            Optional<Merchant> merchantOptional = merchantRepository.findById(request.getMerchantId());
            if (!merchantOptional.isPresent()) {
                throw new MerchantException(HttpStatus.NOT_FOUND, "Merchant didn't not exist");
            }
            merchant = merchantOptional.get();
        }
        if (merchant == null ) {
            throw new MerchantException(HttpStatus.FORBIDDEN, "You don't have permission to do this action");
        }
        List<User> cashiers = userRepository.getCashiers(Role.CASHIER, request.getMerchantId());
        List<Cashier> cashierList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cashiers)) {
            cashierList = cashiers.stream().map(u -> new Cashier().name(u.getProfile()
                    .getFullName())
                    .id(u.getId())
                    .role(Role.CASHIER.name())).collect(Collectors.toList());
        }
        return new CashiersResponse().success(true).message("success").data(cashierList);
    }
}

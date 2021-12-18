package kh.com.sathapana.merchant.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import kh.com.sathapana.merchant.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase
class UserDatabaseAccessTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private Clock clock = Clock.systemUTC();

    @Test
    public void save_2merchant_users_successfully() {
        var userId1 = UUID.randomUUID().toString();
        saveUser(userId1, "lqdung", "0984151324", "Dzung Le", "123456", Role.MERCHANT);
        var userId2 = UUID.randomUUID().toString();
        saveUser(userId2, "lqdung2", "0984151325", "Dzung Le2", "1234567", Role.MERCHANT);
        Assertions.assertTrue( merchantRepository.findMerchantByOwner(userId2).isPresent());
        Assertions.assertTrue(merchantRepository.findMerchantByOwner(userId1).isPresent());
        Assertions.assertEquals(2, userProfileRepository.count());
        Assertions.assertEquals(2, merchantRepository.count());
    }

    @Test
    public void save_2cashier_users_successfully() {
        var userId1 = UUID.randomUUID().toString();
        saveUser(userId1, "lqdung", "0984151324", "Dzung Le", "123456", Role.CASHIER);
        var userId2 = UUID.randomUUID().toString();
        saveUser(userId2, "lqdung2", "0984151325", "Dzung Le2", "1234567", Role.CASHIER);
        Assertions.assertEquals(2, userProfileRepository.count());
        Assertions.assertEquals(0, merchantRepository.count());
    }

    private void saveUser(String userId1, String userName, String phoneNumber, String fullName, String nationalId,
                          Role role) {
        Merchant merchant = null;
        if (role == Role.MERCHANT) {
            merchant = Merchant.builder()
                    .id(UUID.randomUUID().toString())
                    .logo("")
                    .slogan("Whyyy")
                    .webSite("https://google.com")
                    .build();
        }
        userRepository.save(User.builder()
                .username(userName)
                .password("123456x@X")
                .createdAt(clock.instant())
                .updatedAt(clock.instant())
                .role(role)
                .status(UserStatus.ACTIVATED)
                .merchant(merchant)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .phoneNumber(phoneNumber)
                        .email("dzung@gmail.com")
                        .fullName(fullName)
                        .userCIF("11111")
                        .nationalNumber(nationalId)
                        .dateOfBirth(LocalDate.now())
                        .build())
                .id(userId1).build());
    }

}

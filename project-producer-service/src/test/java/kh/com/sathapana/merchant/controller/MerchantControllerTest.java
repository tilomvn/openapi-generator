package kh.com.sathapana.merchant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import kh.com.sathapana.m1_gateway.model.MerchantData;
import kh.com.sathapana.m1_gateway.model.MerchantRegistrationResponse;
import kh.com.sathapana.merchant.common.PasswordUtils;
import kh.com.sathapana.merchant.entity.*;
import kh.com.sathapana.merchant.model.*;
import kh.com.sathapana.merchant.repository.UserRepository;
import kh.com.sathapana.onboarding.model.MerchantActivationResponse;
import kh.com.sathapana.onboarding.model.OnboardingProfile;
import kh.com.sathapana.onboarding.model.UserProfileResponse;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@AutoConfigureEmbeddedDatabase
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MerchantControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private Integer port;

    @Autowired
    private Clock clock;

    @Autowired
    private ObjectMapper objectMapper;

    @ClassRule
    public WireMockClassRule wireMockRule = new WireMockClassRule(9000);

    private static final String ONBOARDING_GET_PROFILE_URL = "/v1/onboarding/profile/";
    private static final String M1_GATEWAY_REGISTER_MERCHANT_API = "/v1/m1-gateway/merchant/registration";

    @BeforeAll
    public void init() {
        wireMockRule.start();
    }

    @AfterAll
    public void destroy() {
        wireMockRule.stop();
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        wireMockRule.resetAll();
    }

    @Test
    void givenAnAvailableUserName_thenGetOKStatus() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var userName = "lqdung1111";
        final var password = "123456x@X";
        var profile = UserProfile.builder()
                .dateOfBirth(LocalDate.now(clock))
                .nationalNumber("43434343434")
                .userCIF("dwudwudiwud")
                .fullName("Dzung Le")
                .email("dzung@gmail.com")
                .phoneNumber("123456789")
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .id(UUID.randomUUID().toString()).build();
        userRepository.save(User.builder()
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .id(userId)
                .username(userName)
                .password(PasswordUtils.hashPassword(password))
                .profile(profile)
                        .merchant(Merchant.builder()
                                .id(UUID.randomUUID().toString())
                                .logo("")
                                .build())
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .build());
        // when
        var responseEntity = testRestTemplate.
                getForEntity(buildRequestUrl("/validation?username=xxxx"), null, GenericResponseBoolean.class);
        // then
        Assertions.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        userRepository.deleteById(userId);
    }

    @Test
    void admin_login_successfully() {
        // given
        var request = new LoginRequest().username("admin").password("123456x@X");
        // when
        var responseEntity = testRestTemplate
                .postForEntity(buildRequestUrl("/login"), request, LoginResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var loginResponse = responseEntity.getBody();
        Assertions.assertEquals(true, loginResponse.getSuccess());
        Assertions.assertEquals(Role.ADMIN.toString(), loginResponse.getData().getRole());
    }

    @Test
    void givenValidRequest_thenLoginSuccessfully() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var userName = "lqdung10";
        final var password = "123456x@X";
        final var profile = UserProfile.builder()
                .dateOfBirth(LocalDate.now(clock))
                .nationalNumber("1111111")
                .userCIF("22222")
                .fullName("Dzung Le")
                .email("dzung@gmail.com")
                .phoneNumber("123456789")
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .id(UUID.randomUUID().toString()).build();
        userRepository.save(User.builder()
                        .role(Role.MERCHANT)
                        .status(UserStatus.ACTIVATED)
                        .id(userId)
                        .username(userName)
                        .password(PasswordUtils.hashPassword(password))
                        .profile(profile)
                        .merchant(Merchant.builder()
                                .id(UUID.randomUUID().toString())
                                .build())
                        .updatedAt(clock.instant())
                        .createdAt(clock.instant())
                .build());
        var request = new LoginRequest().username(userName).password(password);
        // when
        var responseEntity = testRestTemplate
                .postForEntity(buildRequestUrl("/login"), request, LoginResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var loginResponse = responseEntity.getBody();
        Assertions.assertEquals(true, loginResponse.getSuccess());
        Assertions.assertEquals(userId, loginResponse.getData().getUserId());
        Assertions.assertEquals(Role.MERCHANT.toString(), loginResponse.getData().getRole());
        userRepository.deleteById(userId);
    }

    @Test
    void givenAnInValidRequest_thenLoginFailed() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var userName = "lqdung11";
        final var password = "123456x@X";
        final var profile = UserProfile.builder()
                .dateOfBirth(LocalDate.now(clock))
                .nationalNumber("23232323")
                .userCIF("323232323")
                .fullName("Dzung Le")
                .email("dzung@gmail.com")
                .phoneNumber("123456789")
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .id(UUID.randomUUID().toString()).build();
        userRepository.save(User.builder()
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .id(userId)
                .username(userName)
                .password(PasswordUtils.hashPassword("password"))
                .profile(profile)
                        .merchant(Merchant.builder().id(UUID.randomUUID().toString()).build())
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .build());
        var request = new LoginRequest().username(userName).password(password);
        // when
        var responseEntity = testRestTemplate
                .postForEntity(buildRequestUrl("/login"), request, LoginResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        userRepository.deleteById(userId);
    }

    @Test
    void givenAValidUser_thenRegisterMerchantSuccessfully() throws JsonProcessingException {
        // given
        final var merchantId = UUID.randomUUID().toString();
        final var userName = "lqdung";
        final var password = "123456x@X";
        final var onboardingProfile = new OnboardingProfile()
                .dateOfBirth(LocalDate.now(clock))
                .fullName("Dzung Le")
                .userCIF("123456")
                .merchantId(merchantId)
                .nationalId("11111")
                .phoneNumber("123456789")
                .email("dzung@gmail.com");
        final var m1Response = new MerchantRegistrationResponse()
                .data(new MerchantData().merchantId(merchantId))
                .success(true)
                .message("success");
        var onboardingResponse = new UserProfileResponse().success(true).data(onboardingProfile);
        mock_onboarding_succeed_api(merchantId, objectMapper.writeValueAsString(onboardingResponse));
        mock_m1_register_merchant_successfully(merchantId, objectMapper.writeValueAsString(m1Response));
        var request = new RegistrationRequest()
                .merchantId(merchantId)
                .username(userName).password(password);
        // when
        var responseEntity = testRestTemplate
                .postForEntity(buildRequestUrl("/registration"), request, MerchantActivationResponse.class);
        // then
        var optional = userRepository.findById(merchantId);
        var user = optional.get();
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userName, user.getUsername());
        Assertions.assertEquals(Role.MERCHANT, user.getRole());
        Assertions.assertEquals(onboardingProfile.getEmail(), user.getProfile().getEmail());
        Assertions.assertEquals(onboardingProfile.getFullName(), user.getProfile().getFullName());
        Assertions.assertEquals(onboardingProfile.getUserCIF(), user.getProfile().getUserCIF());
        Assertions.assertEquals(onboardingProfile.getNationalId(), user.getProfile().getNationalNumber());
    }

    @Test
    void givenAnIValidUser_thenRegisterMerchantFailed() {
        // given
        final var merchantId = UUID.randomUUID().toString();
        final var userName = "lqdung";
        final var password = "123456x@X";
        mock_onboarding_notfound_api(merchantId);
        var request = new RegistrationRequest()
                .merchantId(merchantId)
                .username(userName).password(password);
        // when
        var responseEntity = testRestTemplate
                .postForEntity(buildRequestUrl("/registration"), request, MerchantActivationResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenACreateUserRequest_thenCreateUserSuccessfully() {
        // given
        var request = new CreateUserRequest().email("dung@gmail.com")
                .fullName("Dzung Le")
                .username("lqdung13ij4i3j4")
                .nationalId("34384834u83u434i3")
                .phoneNumber("1234567")
                .password("123456x@X")
                .type(CreateUserRequest.TypeEnum.CASHIER);
        var headers = new HttpHeaders();
        headers.add("userId", UUID.randomUUID().toString());
        var entity = new RequestEntity(request, headers,  HttpMethod.POST, URI.create(buildRequestUrl("/users/registration")));
        // when
        var responseEntity = testRestTemplate.exchange(entity, CreateUserResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Optional<User> optional = userRepository.findByUsername("lqdung13ij4i3j4");
        Assertions.assertTrue(optional.isPresent());
        var user = optional.get();
        Assertions.assertEquals("lqdung13ij4i3j4", user.getUsername());
    }


    @Test
    void givenAnExistingMerchant_thenGetTheMerchantByOwnerSuccessfully() {
        // given
        var merchantId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        userRepository.save(User.builder()
                        .id(userId)
                        .updatedAt(clock.instant())
                        .createdAt(clock.instant())
                        .username("lqdzung399")
                        .password("123456x@X")
                        .role(Role.MERCHANT)
                        .status(UserStatus.ACTIVATED)
                        .profile(UserProfile.builder()
                                .id(UUID.randomUUID().toString())
                                .fullName("Dzung")
                                .phoneNumber("d0wdi0wd")
                                .dateOfBirth(LocalDate.now())
                                .nationalNumber("9dwdjwdjw9djwd")
                                .userCIF("5454657676t")
                                .email("dzung@gmail.com")
                                .createdAt(clock.instant())
                                .updatedAt(clock.instant())
                                .build())
                        .merchant(Merchant.builder()
                                .id(merchantId)
                                .webSite("https://google.com")
                                .build())
                .build());
        var headers = new HttpHeaders();
        headers.add("userId", userId);
        var entity = new RequestEntity(headers,  HttpMethod.GET, URI.create(buildRequestUrl("/" + merchantId)));
        // when
        var responseEntity = testRestTemplate.exchange(entity, GetMerchantResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var response = responseEntity.getBody();
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertEquals(merchantId, response.getData().getId());
        Assertions.assertEquals(userId, response.getData().getOwnerId());
        userRepository.deleteById(userId);
    }

    @Test
    void givenAnExistingMerchant_thenGetTheMerchantByADMINSuccessfully() {
        // given
        var merchantId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        userRepository.save(User.builder()
                .id(userId)
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung302")
                .password("123456x@X")
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("1234456565336")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("77777dwdwd7")
                        .userCIF("5454657673ef6t")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .merchant(Merchant.builder()
                        .id(merchantId)
                        .webSite("https://google.com")
                        .build())
                .build());
        var optional = userRepository.findByUsername("admin");
        var adminId = optional.get().getId();
        var headers = new HttpHeaders();
        headers.add("userId", adminId);
        var entity = new RequestEntity(headers,  HttpMethod.GET, URI.create(buildRequestUrl("/" + merchantId)));
        // when
        var responseEntity = testRestTemplate.exchange(entity, GetMerchantResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var response = responseEntity.getBody();
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertEquals(merchantId, response.getData().getId());
        Assertions.assertEquals(adminId, response.getData().getOwnerId());
        userRepository.deleteById(userId);
    }

    @Test
    void givenAnExistingMerchant_thenGetTheMerchantByNormalUserFailed() {
        // given
        var merchantId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        userRepository.save(User.builder()
                .id(userId)
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung388")
                .password("123456x@X")
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("w9duw9duwd")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("w0diw0diw0d")
                        .userCIF("5454657676t")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .merchant(Merchant.builder()
                        .id(merchantId)
                        .webSite("https://google.com")
                        .build())
                .build());
        var cashierUser = userRepository.save(User.builder()
                .id(userId)
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung387")
                .password("123456x@X")
                .role(Role.CASHIER)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("dwdiwddw")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("ddwdd")
                        .userCIF("dwd0id9widwd")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .build());
        var headers = new HttpHeaders();
        headers.add("userId", cashierUser.getId());
        var entity = new RequestEntity(headers,  HttpMethod.GET, URI.create(buildRequestUrl("/" + merchantId)));
        // when
        var responseEntity = testRestTemplate.exchange(entity, GetMerchantResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        userRepository.deleteById(userId);
    }

    @Test
    @Disabled
    void given2merhcnat_thenAdminGet2MerchantsSuccessfully() {
        // given
        var merchantId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        userRepository.save(User.builder()
                .id(userId)
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung303")
                .password("123456x@X")
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("12344565656")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("dwidjwidjd")
                        .userCIF("5454657676t")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .merchant(Merchant.builder()
                        .id(merchantId)
                        .webSite("https://google.com")
                        .build())
                .build());
        var merchantId2 = UUID.randomUUID().toString();
        var merchant2 = userRepository.save(User.builder()
                .id(UUID.randomUUID().toString())
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung304")
                .password("123456x@X")
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("1234456533356")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("9988wee")
                        .userCIF("dwd0id9widwd")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .merchant(Merchant.builder()
                        .id(merchantId2)
                        .webSite("https://google.com")
                        .build())
                .build());
        var adminId = userRepository.findByUsername("admin").get().getId();
        var headers = new HttpHeaders();
        headers.add("userId", adminId);
        var entity = new RequestEntity(headers,  HttpMethod.GET, URI.create(buildRequestUrl("/list")));
        // when
        var responseEntity = testRestTemplate.exchange(entity, ListMerchantResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var response = responseEntity.getBody();
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertEquals(2, response.getData().size());
        userRepository.deleteById(userId);
        userRepository.deleteById(merchant2.getId());
    }

    @Test
    @Disabled
    void given2Cashier_thenMerchantGetCashiersSuccessfully() {
        // given
        var merchantId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        userRepository.save(User.builder()
                .id(userId)
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung304")
                .password("123456x@X")
                .role(Role.MERCHANT)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("12344565656")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("dwidjwidjd")
                        .userCIF("5454657676t")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .merchant(Merchant.builder()
                        .id(merchantId)
                        .webSite("https://google.com")
                        .build())
                .build());
        var cashier1 = userRepository.save(User.builder()
                .id(UUID.randomUUID().toString())
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung306")
                .password("123456x@X")
                .role(Role.CASHIER)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("1234ewe343456533356")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("3434343dwdd")
                        .userCIF("dwd0id9w0diwdiwdiw9didwd")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .build());
        var cashier2 = userRepository.save(User.builder()
                .id(UUID.randomUUID().toString())
                .updatedAt(clock.instant())
                .createdAt(clock.instant())
                .username("lqdzung307")
                .password("123456x@X")
                .role(Role.CASHIER)
                .status(UserStatus.ACTIVATED)
                .profile(UserProfile.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("Dzung")
                        .phoneNumber("9dwid9iwdwdwd")
                        .dateOfBirth(LocalDate.now())
                        .nationalNumber("widjiwdjiwjdwjd")
                        .userCIF("wdwdi9di9wd")
                        .email("dzung@gmail.com")
                        .createdAt(clock.instant())
                        .updatedAt(clock.instant())
                        .build())
                .build());
        var headers = new HttpHeaders();
        headers.add("userId", userId);
        var request = new CashierRequest().merchantId(merchantId);
        var entity = new RequestEntity(request, headers, HttpMethod.POST, URI.create(buildRequestUrl("/cashiers")));
        // when
        var responseEntity = testRestTemplate.exchange(entity, CashiersResponse.class);
        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var response = responseEntity.getBody();
        Assertions.assertTrue(response.getSuccess());
        Assertions.assertEquals(2, response.getData().size());
        userRepository.deleteById(userId);
    }

    private String buildRequestUrl(String uri) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://localhost:").append(port).append("/v1/merchant").append(uri);
        return builder.toString();
    }

    private void mock_onboarding_succeed_api(String merchantId, String responseString) {
        wireMockRule.stubFor(get(urlPathMatching(ONBOARDING_GET_PROFILE_URL + merchantId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseString)));

    }

    private void mock_m1_register_merchant_successfully(String merchantId, String responseString) {
        wireMockRule.stubFor(post(urlPathMatching(M1_GATEWAY_REGISTER_MERCHANT_API))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseString)));

    }

    private void mock_onboarding_notfound_api(String merchantId) {
        wireMockRule.stubFor(get(urlPathMatching(ONBOARDING_GET_PROFILE_URL + merchantId))
                .willReturn(aResponse()
                        .withStatus(404)));
    }
}
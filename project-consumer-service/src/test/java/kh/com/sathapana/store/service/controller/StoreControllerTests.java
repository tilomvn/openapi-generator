package kh.com.sathapana.store.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import kh.com.sathapana.merchant.model.Cashier;
import kh.com.sathapana.merchant.model.CashiersResponse;
import kh.com.sathapana.store.service.utils.JWTTokenService;
import kh.com.sathapana.store.model.*;
import kh.com.sathapana.store.service.utils.Message;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JWTTokenService jwtTokenService;

    @Autowired
    ObjectMapper objectMapper;

    @ClassRule
    WireMockClassRule wireMock = new WireMockClassRule(9000);

    @LocalServerPort
    private int port;

    @BeforeAll
    void init() {
        wireMock.start();
    }

    @AfterAll
    void destroy() {
        wireMock.shutdown();
    }

    @Test
    @DisplayName("Get Store")
    void testGetStore() throws Exception {
        var storeId = UUID.randomUUID().toString();
        var userId = UUID.randomUUID().toString();
        var role = "MERCHANT";
        var token = jwtTokenService.generateToken(userId, role);

        wireMock.stubFor(WireMock.post(urlPathMatching("/v1/merchant/cashiers"))
        .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(new CashiersResponse().success(true).data(Arrays.asList(
                        new Cashier().name("A"), new Cashier().name("B")
                ))))));

        var result = mockMvc.perform(get("/v1/stores/" + storeId)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), StoreDetailResponse.class);
        assertEquals(Message.SUCCESS, response.getMessage());
    }

    @Test
    @DisplayName("Register New User Success")
    void testRegisterUserSuccess() throws Exception {
        var userId = UUID.randomUUID().toString();
        var role = "MERCHANT";
        var token = jwtTokenService.generateToken(userId, role);

        var reqBody = "{\"id\": \"" + userId + "\", \"name\": \"name\"}";
        var result = mockMvc.perform(multipart("/v1/stores/user")
                        .file(new MockMultipartFile("logo", new ClassPathResource("logo.png").getInputStream().readAllBytes()))
                        .file(new MockMultipartFile("user", null, MediaType.APPLICATION_JSON_VALUE, reqBody.getBytes()))
                        .header(HttpHeaders.AUTHORIZATION, token).header("userId", userId))
                .andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), UserRegistrationResponse.class);
        assertEquals(Message.SUCCESS, response.getMessage());
        assertEquals(true, response.getSuccess());
        assertTrue(response.getData() != null);
        assertEquals(userId, response.getData().getId());
    }
}

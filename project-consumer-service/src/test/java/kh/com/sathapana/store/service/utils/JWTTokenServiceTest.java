package kh.com.sathapana.store.service.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JWTTokenServiceTest {

    private final JWTTokenService jwtTokenService = new JWTTokenService("Rfk45@#!FFfwef", 900000L,
            "Bearer", "sathapanaBank", Clock.systemUTC());

    @Test
    @DisplayName("Generate Token")
    public void testGenerateToken() {
        var userId = UUID.randomUUID().toString();
        var role = "MERCHANT";

        var token = jwtTokenService.generateToken(userId, role);

        var stringClaimMap = jwtTokenService.verifyAndGetPayload(token);
        assertEquals(role, stringClaimMap.get("role").asString());
        assertEquals(userId, stringClaimMap.get("userId").asString());
    }
}
package kh.com.sathapana.store.service.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenService {
    private final String secret;

    private final Long expireTime;

    private final String tokenPrefix;

    private final Clock clock;
    private final String issuer;

    public JWTTokenService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expire}") Long expire,
                      @Value("${security.jwt.prefix:Bearer}") String tokenPrefix,
                      @Value("${security.jwt.issuer}") String issuer,
                      Clock clock) {
        this.secret = secret;
        this.expireTime = expire;
        this.tokenPrefix = tokenPrefix;
        this.clock = clock;
        this.issuer = issuer;
    }

    /**
     * TODO there will be more things to do with this, need to clarify more.
     * @param role
     * @return
     */
    public String generateToken(String userId, String role) {
        var payload = new HashMap<String, Object>();
        payload.put("userId", userId);
        payload.put("role", role);
        return JWT.create()
                .withSubject(userId)
                .withPayload(payload)
                .withExpiresAt(new Date(clock.millis() + expireTime))
                .withIssuedAt(new Date(clock.millis()))
                .withIssuer(issuer)
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    /**
     * For testing only.
     * @param token
     * @return
     */
    public Map<String, Claim> verifyAndGetPayload(String token) {
        return JWT.require(Algorithm.HMAC256(secret.getBytes()))
                .build()
                .verify(token)
                .getClaims();
    }
}

package com.saea.bookecommerce.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(String username, String role) {
        long expiresAt = Instant.now().toEpochMilli() + expirationMs;
        String payload = username + ":" + role + ":" + expiresAt;
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return encodedPayload + "." + sign(encodedPayload);
    }

    public String getUsername(String token) {
        return parsePayload(token)[0];
    }

    public String getRole(String token) {
        return parsePayload(token)[1];
    }

    public boolean isValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 2) {
                return false;
            }
            if (!sign(parts[0]).equals(parts[1])) {
                return false;
            }
            long expiresAt = Long.parseLong(parsePayload(token)[2]);
            return expiresAt > Instant.now().toEpochMilli();
        } catch (Exception exception) {
            return false;
        }
    }

    private String[] parsePayload(String token) {
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[0]), StandardCharsets.UTF_8);
        return payload.split(":", 3);
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);
            byte[] signature = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not create token signature");
        }
    }
}

package com.omdb.api.config.security;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger tokenLogger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private SecretKey secretKey;
    private final long expirationTime;
    private final String secretKeyString;

    public JwtTokenProvider(@Value("${spring.security.jwt.expiration}") long expirationTime,
                            @Value("${spring.security.jwt.secret}") String secretKeyString) {
        this.expirationTime = expirationTime;
        this.secretKeyString = secretKeyString;
    }

    @PostConstruct
    public void initializeSecretKey() {
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA256");
    }

    public String createToken(String username, String email) {

        tokenLogger.info("create token for user {}", username + " - " + email);

        Map<String, String> mapClaims = new HashMap<>();
        mapClaims.put("username", username);
        mapClaims.put("email", email);

        Instant creationTime = Instant.now();
        Instant expirationTime = creationTime.plus(this.expirationTime, ChronoUnit.HOURS);

        return Jwts.builder()
                .claims(mapClaims)
                .subject(email)
                .issuedAt(Date.from(creationTime))
                .expiration(Date.from(expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey)
                    .build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
}

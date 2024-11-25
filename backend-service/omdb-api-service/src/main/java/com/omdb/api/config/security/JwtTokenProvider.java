package com.omdb.api.config.security;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

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
        // Generar clave secreta a partir de la cadena
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA256");
    }

    public String createToken(String username, String email) {
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

    public String validateToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .build().parseSignedClaims(token).toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
}

package com.mygym.app.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility component for generating, parsing, and validating Nimbus JWT security tokens.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String encryptionSecret;

    private SecretKey getSigningKey() {
        return new SecretKeySpec(encryptionSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public String generateToken(String username, String role) {
        try {
            long tenHoursInMillis = 1000L * 60 * 60 * 10;
            Date now = new Date();

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("role", role)
                    .issueTime(now)
                    .expirationTime(new Date(now.getTime() + tenHoursInMillis))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(new MACSigner(getSigningKey()));
            
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate native token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim("role");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isVerified = signedJWT.verify(new MACVerifier(getSigningKey()));
            boolean isExpired = signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
            String extractedUser = signedJWT.getJWTClaimsSet().getSubject();

            return isVerified && !isExpired && extractedUser.equals(username);
        } catch (Exception e) {
            return false;
        }
    }
}
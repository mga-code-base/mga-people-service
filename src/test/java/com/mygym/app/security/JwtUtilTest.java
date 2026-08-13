package com.mygym.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "superSecretKeyForJwtSigningThatIsAtLeast256BitsLongForHMACSHA256!";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "encryptionSecret", testSecret);
    }

    @Test
    @DisplayName("Should successfully generate token and extract username and role")
    void testGenerateTokenAndExtract_Success() {
        String username = "people_admin";
        String role = "ADMIN";

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        assertEquals(username, jwtUtil.extractUsername(token));
        assertEquals(role, jwtUtil.extractRole(token));
    }

    @Test
    @DisplayName("Should validate token successfully for matching username")
    void testValidateToken_ValidToken_ReturnsTrue() {
        String username = "gym_trainer";
        String token = jwtUtil.generateToken(username, "TRAINER");

        boolean isValid = jwtUtil.validateToken(token, username);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should fail token validation when username mismatches")
    void testValidateToken_MismatchedUsername_ReturnsFalse() {
        String token = jwtUtil.generateToken("user_a", "MEMBER");

        boolean isValid = jwtUtil.validateToken(token, "user_b");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when validating malformed token string")
    void testValidateToken_MalformedToken_ReturnsFalse() {
        boolean isValid = jwtUtil.validateToken("invalid.token.string", "user_a");

        assertFalse(isValid);
    }
}
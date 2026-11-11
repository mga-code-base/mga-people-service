package com.mygym.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mygym.app.dto.AuthResponse;
import com.mygym.app.dto.LoginRequest;
import com.mygym.app.models.Admin;
import com.mygym.app.security.JwtUtil;
import com.mygym.app.service.AdminServices;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminServices service;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AdminController controller;

    @Test
    @DisplayName("Should issue a token to an active admin")
    void testActiveAdminGetsToken() {
        LoginRequest request = new LoginRequest("admin.alex", "S3cret!pass");
        when(service.authenticate(request)).thenReturn(admin(true));
        when(jwtUtil.generateToken("admin.alex", "ADMIN")).thenReturn("token");

        ResponseEntity<AuthResponse> response = controller.loginAdmin(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().token());
    }

    @Test
    @DisplayName("Should refuse a token to an admin who has not been approved")
    void testInactiveAdminIsRefused() {
        LoginRequest request = new LoginRequest("admin.alex", "S3cret!pass");
        when(service.authenticate(request)).thenReturn(admin(false));

        ResponseEntity<AuthResponse> response = controller.loginAdmin(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse(response.getBody().isActive());
        assertNull(response.getBody().token());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Should answer 401 for unknown credentials")
    void testUnknownAdminIsUnauthorized() {
        LoginRequest request = new LoginRequest("nobody", "wrong-password");
        when(service.authenticate(request)).thenReturn(Admin.empty());

        assertEquals(HttpStatus.UNAUTHORIZED, controller.loginAdmin(request).getStatusCode());
    }

    private static Admin admin(boolean active) {
        return new Admin(null, "7", "Alex Rao", "admin.alex", null, "9876543210", "alex@mygym.app", active, "3");
    }
}

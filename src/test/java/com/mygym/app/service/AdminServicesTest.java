package com.mygym.app.service;

import com.mygym.app.models.Admin;
import com.mygym.app.models.AdminWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServicesTest {

    @Mock
    private org.springframework.web.client.RestClient restAdminClient;

    @Mock
    private org.springframework.web.client.RestClient restBranchClient;

    @Mock
    private org.springframework.web.client.RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private org.springframework.web.client.RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private org.springframework.web.client.RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private AdminServices adminServices;

    @Test
    @DisplayName("Should fetch admin details by admin id")
    void testGetAdminById_Success() {
        Admin admin = new Admin();
        admin.setAdminId("101");

        when(restAdminClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{id}"), eq("101"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Admin.class)).thenReturn(admin);

        Admin result = adminServices.getAdminById("101");

        assertNotNull(result);
        assertEquals("101", result.getAdminId());
    }

    @Test
    @DisplayName("Should return primaryAdminCheck 1 when primary admin ID is '0'")
    void testPrimaryAdminCheck_Exists_ReturnsOne() {
        Admin admin = new Admin();
        admin.setAdminId("0");

        when(restAdminClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{id}"), eq(0))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Admin.class)).thenReturn(admin);

        Short check = adminServices.primaryAdminCheck();

        assertEquals((short) 1, check);
    }

    @Test
    @DisplayName("Should return primaryAdminCheck -1 when downstream CRUD call throws exception")
    void testPrimaryAdminCheck_Error_ReturnsMinusOne() {
        when(restAdminClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{id}"), eq(0))).thenThrow(new RuntimeException("CRUD Offline"));

        Short check = adminServices.primaryAdminCheck();

        assertEquals((short) -1, check);
    }
}
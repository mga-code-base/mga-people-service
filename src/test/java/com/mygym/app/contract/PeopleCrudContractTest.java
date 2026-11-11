package com.mygym.app.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.mygym.app.dto.LoginRequest;
import com.mygym.app.models.Admin;
import com.mygym.app.service.AdminServices;

@Tag("contract")
class PeopleCrudContractTest {

    private static final int STUB_PORT = 18080;

    @RegisterExtension
    static StubRunnerExtension stubRunner = new StubRunnerExtension()
            .downloadStub("com.mygym.app", "mga-people-crud-service", "+", "stubs")
            .withPort(STUB_PORT)
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);

    private AdminServices adminServices;

    @BeforeEach
    void setUp() {
        RestClient admins = RestClient.builder()
                .baseUrl("http://localhost:" + STUB_PORT + "/crud/admins")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        adminServices = new AdminServices(admins, admins);
    }

    @Test
    @DisplayName("Should read the admin returned for valid credentials")
    void testAuthenticateWithValidCredentials() {
        Admin admin = adminServices.authenticate(new LoginRequest("admin.alex", "S3cret!pass"));

        assertEquals("7", admin.adminId());
        assertEquals("3", admin.branchId());
        assertTrue(admin.isActive());
        assertNull(admin.password());
    }

    @Test
    @DisplayName("Should get an admin without an id for unknown credentials")
    void testAuthenticateWithInvalidCredentials() {
        Admin admin = adminServices.authenticate(new LoginRequest("someone.else", "wrong-password"));

        assertNull(admin.adminId());
    }

    @Test
    @DisplayName("Should fetch an admin by id")
    void testGetAdminById() {
        Admin admin = adminServices.getAdminById("7");

        assertEquals("admin.alex", admin.username());
    }
}

package com.mygym.app.service;

import com.mygym.app.models.Member;
import com.mygym.app.models.MemberWrapper;
import com.mygym.app.models.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServicesTest {

    @Mock
    private RestClient restMemberClient;

    @Mock
    private RestClient restTrainerClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private MemberServices memberServices;

    @BeforeEach
    void setUp() {
        memberServices = new MemberServices(restMemberClient, restTrainerClient);
    }

    @Test
    @DisplayName("Should fetch member details by username")
    void testGetMemberByUsername_Success() {
        Member member = new Member(null, null, null, null, null, null, null, null, null, null, "john_doe", null, null, null, null, null);

        when(restMemberClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/username/{username}"), eq("john_doe"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Member.class)).thenReturn(member);

        Member result = memberServices.getMemberByUsername("john_doe");

        assertNotNull(result);
        assertEquals("john_doe", result.username());
    }

    @Test
    @DisplayName("Should return false when assigning invalid trainer to member")
    void testUpdateTrainerForMember_InvalidTrainer_ReturnsFalse() {
        Trainer invalidTrainer = Trainer.notFound();

        when(restTrainerClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{id}"), eq("T-99"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Trainer.class)).thenReturn(invalidTrainer);

        Boolean result = memberServices.updateTrainerForMember("M-101", "T-99");

        assertFalse(result);
    }
}
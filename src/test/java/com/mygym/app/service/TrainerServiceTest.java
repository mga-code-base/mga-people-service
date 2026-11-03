package com.mygym.app.service;

import com.mygym.app.models.Branch;
import com.mygym.app.models.Trainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient restBranchClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    @DisplayName("Should approve trainer status downstream")
    void testApproveTrainer_Success() {
        when(restClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/approve/{trainerid}"), eq("T-201"))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Boolean.class)).thenReturn(true);

        Boolean approved = trainerService.approveTrainer("T-201");

        assertTrue(approved);
    }

    @Test
    @DisplayName("Should return branch details associated with trainer profile")
    void testFindTrainerBranch_Success() {
        Trainer trainer = new Trainer(null, "T-201", null, null, null, null, null, null, "B-10", null);

        Branch branch = new Branch(null, "B-10", null, null, null, null, null, null, null);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{id}"), eq("T-201"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Trainer.class)).thenReturn(trainer);

        when(restBranchClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{id}"), eq("B-10"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Branch.class)).thenReturn(branch);

        Branch result = trainerService.findTrainerBranch("T-201");

        assertNotNull(result);
        assertEquals("B-10", result.branchId());
    }
}
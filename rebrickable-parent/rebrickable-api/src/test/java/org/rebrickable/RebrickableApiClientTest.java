package org.rebrickable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

class RebrickableApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RebrickableApiClient apiClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFetchSetDetails() {
        String setNum = "1234-1";
        Set expectedSet = new Set();
        expectedSet.setSetNum(setNum);
        expectedSet.setName("Test Set");

        when(restTemplate.getForObject(anyString(), eq(Set.class))).thenReturn(expectedSet);

        Set actualSet = apiClient.getSetDetails(setNum);

        assertNotNull(actualSet);
        assertEquals("1234-1", actualSet.getSetNum());
        assertEquals("Test Set", actualSet.getName());
    }
}

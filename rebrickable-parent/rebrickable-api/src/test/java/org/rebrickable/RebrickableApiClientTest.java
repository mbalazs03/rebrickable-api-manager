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

    @Test
    void searchSetsShouldStripApiKeyFromPaginationLinks() {
        RebrickableApiClient client = new RebrickableApiClient(restTemplate, "secret-key");

        RebrickableResponse upstream = new RebrickableResponse();
        upstream.setNext("https://rebrickable.com/api/v3/lego/sets/?key=secret-key&page=2");
        upstream.setPrevious("https://rebrickable.com/api/v3/lego/sets/?key=secret-key&page=1");
        when(restTemplate.getForObject(anyString(), eq(RebrickableResponse.class))).thenReturn(upstream);

        RebrickableResponse response = client.searchSets(null, null, null, null, null, 2, 10);

        assertFalse(response.getNext().contains("secret-key"));
        assertFalse(response.getPrevious().contains("secret-key"));
        assertTrue(response.getNext().startsWith("/api/rebrickable/sets/search"));
    }

    @Test
    void getSetPartsShouldStripApiKeyFromPaginationLinks() {
        RebrickableApiClient client = new RebrickableApiClient(restTemplate, "secret-key");

        RebrickablePartResponse upstream = new RebrickablePartResponse();
        upstream.setNext("https://rebrickable.com/api/v3/lego/sets/1234-1/parts/?key=secret-key&page=2");
        upstream.setPrevious(null);
        when(restTemplate.getForObject(anyString(), eq(RebrickablePartResponse.class))).thenReturn(upstream);

        RebrickablePartResponse response = client.getSetParts("1234-1", 2, 10);

        assertFalse(response.getNext().contains("secret-key"));
        assertNotNull(response.getNext());
        assertNull(response.getPrevious());
    }
}

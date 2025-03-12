package org.rebrickable.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rebrickable.RebrickableApiClient;
import org.rebrickable.RebrickableResponse;
import org.rebrickable.Set;
import org.rebrickable.SetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class RebrickableServiceTest {

    @Mock
    private RebrickableApiClient apiClient;

    @Mock
    private SetRepository setRepository;

    @InjectMocks
    private RebrickableService service;

    @Test
    void shouldReturnSetDetails() {
        // Given
        String setNum = "10214-1";
        Set expectedSet = new Set();
        expectedSet.setSetNum(setNum);
        expectedSet.setName("Tower Bridge");

        when(apiClient.getSetDetails(setNum)).thenReturn(expectedSet);

        Set actualSet = service.getSetDetails(setNum);

        assertNotNull(actualSet);
        assertEquals("Tower Bridge", actualSet.getName());
        verify(apiClient, times(1)).getSetDetails(setNum);
    }

    @Test
    void shouldSearchSets() {
        String query = "Star Wars";
        RebrickableResponse expectedResponse = new RebrickableResponse();
        expectedResponse.setCount(1);
        expectedResponse.setResults(List.of(new Set()));

        when(apiClient.searchSets(eq(query), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(expectedResponse);

        RebrickableResponse actualResponse = service.searchSets(query, null, null, null, null, 1, 10);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getResults().size());
    }
}

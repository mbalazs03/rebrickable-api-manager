package org.rebrickable.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.rebrickable.*;
import org.rebrickable.Set;
import org.rebrickable.config.GlobalExceptionHandler;
import org.rebrickable.service.RebrickableService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@SpringBootTest
@Import({GlobalExceptionHandler.class, RebrickableController.class})
public class RebrickableControllerTest {

    @Mock
    private RebrickableService rebrickableService;

    private RebrickableController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new RebrickableController(rebrickableService);
    }

    @Test
    public void testGetSetDetails() {
        String setNum = "12345";
        Set expectedSet = new Set();
        expectedSet.setSetNum(setNum);
        expectedSet.setName("Test Set");

        when(rebrickableService.getSetDetails(setNum)).thenReturn(expectedSet);

        Set actualSet = controller.getSetDetails(setNum);

        assertEquals(expectedSet, actualSet);
        verify(rebrickableService).getSetDetails(setNum);
    }

    @Test
    public void testSearchSets() {
        RebrickableResponse expectedResponse = new RebrickableResponse();
        expectedResponse.setCount(1);
        expectedResponse.setResults(Collections.singletonList(new Set()));

        when(rebrickableService.searchSets("query", "setNum", "name", 2000, 2020, 1, 12))
                .thenReturn(expectedResponse);

        RebrickableResponse actualResponse = controller.searchSets("query", "setNum", "name", 2000, 2020, 1, 12);

        assertEquals(expectedResponse, actualResponse);
        verify(rebrickableService).searchSets("query", "setNum", "name", 2000, 2020, 1, 12);
    }

    @Test
    public void testGetSetParts() {
        String setNum = "12345";
        RebrickablePartResponse expectedResponse = new RebrickablePartResponse();
        expectedResponse.setCount(10);
        expectedResponse.setResults(new ArrayList<>());

        when(rebrickableService.getSetParts(setNum, 1, 10)).thenReturn(expectedResponse);

        RebrickablePartResponse actualResponse = controller.getSetParts(setNum, 1, 10);

        assertEquals(expectedResponse, actualResponse);
        verify(rebrickableService).getSetParts(setNum, 1, 10);
    }

    @Test
    public void testGetBuildableSets() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("testuser", null));
        SecurityContextHolder.setContext(securityContext);

        List<BuildableSetResponse> expectedList = new ArrayList<>();
        expectedList.add(new BuildableSetResponse(new Set(), 100.0, Collections.emptyList()));

        when(rebrickableService.getBuildableSets("testuser", "query", "setNum", "name", 2000, 2020, 1, 10))
                .thenReturn(expectedList);

        ResponseEntity<?> responseEntity = controller.getBuildableSets("query", "setNum", "name", 2000, 2020, 1, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedList, responseEntity.getBody());
    }

    @Test
    public void testSaveSet() {
        Set set = new Set();
        set.setSetNum("12345");
        set.setName("Test Set");

        controller.saveSet(set);

        verify(rebrickableService).saveSet(set);
    }
}

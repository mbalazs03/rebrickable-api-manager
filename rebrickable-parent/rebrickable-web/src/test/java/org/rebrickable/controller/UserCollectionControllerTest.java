package org.rebrickable.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.rebrickable.Set;
import org.rebrickable.SetRepository;
import org.rebrickable.config.GlobalExceptionHandler;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@SpringBootTest
@Import({GlobalExceptionHandler.class, UserCollectionController.class})
public class UserCollectionControllerTest {

    @Mock
    private SetRepository setRepository;

    private UserCollectionController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserCollectionController(setRepository);

        // Set up a dummy authentication with username "testuser"
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("testuser", null));
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetOwnedSets() {
        List<Set> expectedSets = Arrays.asList(new Set(), new Set());
        when(setRepository.findByOwner("testuser")).thenReturn(expectedSets);

        List<Set> actualSets = controller.getOwnedSets();

        assertEquals(expectedSets, actualSets);
        verify(setRepository).findByOwner("testuser");
    }

    @Test
    public void testUpdateSetOwnership_OwnedTrue() {
        String setNum = "12345";
        Set set = new Set();
        set.setSetNum(setNum);
        set.setOwner(null);

        when(setRepository.findById(setNum)).thenReturn(Optional.of(set));
        when(setRepository.save(any(Set.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set updatedSet = controller.updateSetOwnership(setNum, true);

        assertEquals("testuser", updatedSet.getOwner());
        verify(setRepository).findById(setNum);
        verify(setRepository).save(set);
    }

    @Test
    public void testUpdateSetOwnership_OwnedFalse() {
        String setNum = "12345";
        Set set = new Set();
        set.setSetNum(setNum);
        set.setOwner("testuser");

        when(setRepository.findById(setNum)).thenReturn(Optional.of(set));
        when(setRepository.save(any(Set.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set updatedSet = controller.updateSetOwnership(setNum, false);

        assertNull(updatedSet.getOwner());
        verify(setRepository).findById(setNum);
        verify(setRepository).save(set);
    }

    @Test
    public void testUpdateSetOwnership_SetNotFound() {
        String setNum = "nonexistent";
        when(setRepository.findById(setNum)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.updateSetOwnership(setNum, true);
        });

        assertEquals("Set not found with setNum: " + setNum, exception.getMessage());
        verify(setRepository).findById(setNum);
        verify(setRepository, never()).save(any(Set.class));
    }
}

package org.rebrickable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class SetTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenSetCreated_thenPropertiesAreCorrect() {
        String setNum = "42083-1";
        String name = "Bugatti Chiron";
        Integer year = 2018;
        Integer themeId = 1;
        Integer numParts = 3599;
        String setImgUrl = "https://example.com/image.jpg";
        String setUrl = "https://example.com/set";
        String lastModifiedDt = "2023-01-01";
        String owner = "testUser";
        
        Set set = new Set();
        set.setSetNum(setNum);
        set.setName(name);
        set.setYear(year);
        set.setThemeId(themeId);
        set.setNumParts(numParts);
        set.setSetImgUrl(setImgUrl);
        set.setSetUrl(setUrl);
        set.setLastModifiedDt(lastModifiedDt);
        set.setOwner(owner);
        
        assertEquals(setNum, set.getSetNum());
        assertEquals(name, set.getName());
        assertEquals(year, set.getYear());
        assertEquals(themeId, set.getThemeId());
        assertEquals(numParts, set.getNumParts());
        assertEquals(setImgUrl, set.getSetImgUrl());
        assertEquals(setUrl, set.getSetUrl());
        assertEquals(lastModifiedDt, set.getLastModifiedDt());
        assertEquals(owner, set.getOwner());
    }
    
    @Test
    void whenToStringCalled_thenCorrectStringReturned() {
        Set set = new Set();
        set.setSetNum("42083-1");
        set.setName("Bugatti Chiron");
        
        String result = set.toString();
        
        assertTrue(result.contains("42083-1"));
        assertTrue(result.contains("Bugatti Chiron"));
    }

    @Test
    void whenSetNumIsBlank_thenValidationFails() {

        Set set = new Set();
        set.setSetNum("");
        set.setName("Test Set");
        set.setYear(2023);

        java.util.Set<ConstraintViolation<org.rebrickable.Set>> violations = validator.validate(set);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Set number cannot be blank", 
            violations.iterator().next().getMessage());
    }

    @Test
    void whenYearIsBelow1900_thenValidationFails() {

        Set set = new Set();
        set.setSetNum("42083-1");
        set.setName("Test Set");
        set.setYear(1899);

        java.util.Set<ConstraintViolation<org.rebrickable.Set>> violations = validator.validate(set);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Year must be after 1900", 
            violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsValid_thenValidationSucceeds() {

        Set set = new Set();
        set.setSetNum("42083-1");
        set.setName("Test Set");
        set.setYear(2023);
        set.setThemeId(1);
        set.setNumParts(100);

        // Act
        java.util.Set<ConstraintViolation<org.rebrickable.Set>> violations = validator.validate(set);

        assertTrue(violations.isEmpty());
    }
}
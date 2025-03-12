package org.rebrickable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PartTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenPartCreated_thenPropertiesAreCorrect() {
        String partNum = "3001";
        String name = "Brick 2 x 4";
        String color = "Red";
        int quantity = 5;
        String partImgUrl = "https://example.com/part.jpg";
        
        Part part = new Part();
        part.setPartNum(partNum);
        part.setName(name);
        part.setColor(color);
        part.setQuantity(quantity);
        part.setPartImgUrl(partImgUrl);
        
        assertEquals(partNum, part.getPartNum());
        assertEquals(name, part.getName());
        assertEquals(color, part.getColor());
        assertEquals(quantity, part.getQuantity());
        assertEquals(partImgUrl, part.getPartImgUrl());
    }
    
    @Test
    void whenToStringCalled_thenCorrectStringReturned() {
        Part part = new Part();
        part.setPartNum("3001");
        part.setName("Brick 2 x 4");
        
        String result = part.toString();
        
        assertTrue(result.contains("3001"));
        assertTrue(result.contains("Brick 2 x 4"));
    }

    @Test
    void whenPartNumIsBlank_thenValidationFails() {
        Part part = new Part();
        part.setPartNum("");
        part.setName("Test Part");
        part.setColor("Red");
        part.setQuantity(1);

        Set<ConstraintViolation<Part>> violations = validator.validate(part);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Part number cannot be blank", 
            violations.iterator().next().getMessage());
    }

    @Test
    void whenQuantityIsNegative_thenValidationFails() {
        Part part = new Part();
        part.setPartNum("3001");
        part.setName("Test Part");
        part.setColor("Red");
        part.setQuantity(-1);

        Set<ConstraintViolation<Part>> violations = validator.validate(part);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity cannot be negative", 
            violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsValid_thenValidationSucceeds() {
        Part part = new Part();
        part.setPartNum("3001");
        part.setName("Test Part");
        part.setColor("Red");
        part.setQuantity(1);

        Set<ConstraintViolation<Part>> violations = validator.validate(part);

        assertTrue(violations.isEmpty());
    }
} 
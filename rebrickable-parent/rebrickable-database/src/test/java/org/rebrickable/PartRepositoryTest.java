package org.rebrickable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootTest(classes = PartRepositoryTest.class)
public class PartRepositoryTest {

    @Autowired
    private PartRepository partRepository;

    @Test
    public void testFindByNameContainingIgnoreCase() {
        Part part = new Part();
        part.setPartNum("3001");
        part.setName("Brick 2x4");
        part.setColor("Red");
        part.setQuantity(100);
        part.setPartImgUrl("https://example.com/brick.jpg");

        partRepository.save(part);

        List<Part> parts = partRepository.findByNameContainingIgnoreCase("brick");
        assertThat(parts).isNotEmpty();
        assertThat(parts.getFirst().getName()).containsIgnoringCase("brick");
    }
}

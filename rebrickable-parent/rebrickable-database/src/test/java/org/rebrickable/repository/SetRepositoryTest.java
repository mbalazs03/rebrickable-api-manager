package org.rebrickable.repository;

import org.junit.jupiter.api.Test;
import org.rebrickable.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootTest(classes = SetRepositoryTest.class)
public class SetRepositoryTest {

    @Autowired
    private SetRepository setRepository;

    @Test
    public void testFindByNameContainingIgnoreCase() {
        Set legoSet = new Set();
        legoSet.setSetNum("1234");
        legoSet.setName("Millennium Falcon");
        legoSet.setYear(2019);
        legoSet.setOwner("user1");
        setRepository.save(legoSet);

        List<Set> results = setRepository.findByNameContainingIgnoreCase("falcon");
        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getName()).containsIgnoringCase("falcon");
    }

    @Test
    public void testFindByOwner() {
        Set legoSet = new Set();
        legoSet.setSetNum("1235");
        legoSet.setName("X-wing");
        legoSet.setYear(2018);
        legoSet.setOwner("user2");
        setRepository.save(legoSet);

        List<Set> results = setRepository.findByOwner("user2");
        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getName()).isEqualTo("X-wing");
    }
}

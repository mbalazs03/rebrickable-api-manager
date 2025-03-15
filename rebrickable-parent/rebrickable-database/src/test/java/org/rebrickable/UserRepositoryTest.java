package org.rebrickable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootTest(classes = UserRepositoryTest.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Generate a unique identifier for username and email
        String uniqueId = UUID.randomUUID().toString();
        String username = "testuser-" + uniqueId;
        String email = "testuser-" + uniqueId + "@example.com";

        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setEmail(email);
        userRepository.save(user);

        User found = userRepository.findByUsername(username).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(email);
    }
}

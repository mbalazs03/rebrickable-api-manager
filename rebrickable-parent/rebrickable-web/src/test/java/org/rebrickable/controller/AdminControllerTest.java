package org.rebrickable.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.rebrickable.User;
import org.rebrickable.repository.UserRepository;
import org.rebrickable.config.GlobalExceptionHandler;
import org.rebrickable.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
@Import(GlobalExceptionHandler.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    // GET /api/admin/users
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers_asAdmin() throws Exception {
        User user1 = new User();
        user1.setId("1");
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setRole("USER");

        User user2 = new User();
        user2.setId("2");
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setRole("ADMIN");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].email", is("user1@example.com")))
                .andExpect(jsonPath("$[0].role", is("USER")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].username", is("user2")))
                .andExpect(jsonPath("$[1].email", is("user2@example.com")))
                .andExpect(jsonPath("$[1].role", is("ADMIN")));
    }

    // POST /api/admin/promote/{id}
    @Test
    @WithMockUser(roles = "ADMIN")
    void testPromoteUser_Success() throws Exception {
        User user = new User();
        user.setId("100");
        user.setUsername("regularUser");
        user.setEmail("regular@example.com");
        user.setRole("USER");

        when(userRepository.findById("100")).thenReturn(Optional.of(user));

        // Az adminná emelés során a role "ADMIN" lesz
        user.setRole("ADMIN");
        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(post("/api/admin/promote/{id}", "100").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("100")))
                .andExpect(jsonPath("$.username", is("regularUser")))
                .andExpect(jsonPath("$.email", is("regular@example.com")))
                .andExpect(jsonPath("$.role", is("ADMIN")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPromoteUser_NotFound() throws Exception {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/admin/promote/{id}", "999").with(csrf()))
                .andExpect(content().string(containsString("User not found")));
    }

    // POST /api/admin/revoke/{id}
    @Test
    @WithMockUser(roles = "ADMIN")
    void testRevokeUser_Success() throws Exception {
        User user = new User();
        user.setId("200");
        user.setUsername("adminUser");
        user.setEmail("admin@example.com");
        user.setRole("ADMIN");

        when(userRepository.findById("200")).thenReturn(Optional.of(user));

        // A revoke során a role "USER" lesz
        user.setRole("USER");
        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(post("/api/admin/revoke/{id}", "200").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("200")))
                .andExpect(jsonPath("$.username", is("adminUser")))
                .andExpect(jsonPath("$.email", is("admin@example.com")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRevokeUser_NotFound() throws Exception {
        when(userRepository.findById("321")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/admin/revoke/321", "321").with(csrf()))
                .andExpect(content().string(containsString("User not found")));
    }
    // DELETE /api/admin/users/{id}
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser_Success() throws Exception {
        when(userRepository.existsById("5")).thenReturn(true);

        mockMvc.perform(delete("/api/admin/users/{id}", "5").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User deleted successfully")));

        verify(userRepository).deleteById("5");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser_NotFound() throws Exception {
        when(userRepository.existsById("42")).thenReturn(false);

        mockMvc.perform(delete("/api/admin/users/{id}", "42").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));

        verify(userRepository, never()).deleteById("42");
    }

    // POST /api/admin/impersonate/{id}
    @Test
    @WithMockUser(roles = "ADMIN")
    void testImpersonateUser_Success() throws Exception {
        User user = new User();
        user.setId("10");
        user.setUsername("targetUser");
        user.setEmail("target@example.com");
        user.setRole("USER");

        when(userRepository.findById("10")).thenReturn(Optional.of(user));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("targetUser")
                .password("dummy")
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername("targetUser")).thenReturn(userDetails);
        String token = "dummy-jwt-token";
        when(jwtUtil.generateToken(userDetails)).thenReturn(token);

        mockMvc.perform(post("/api/admin/impersonate/{id}", "10").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token)))
                .andExpect(jsonPath("$.username", is("targetUser")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testImpersonateUser_NotFound() throws Exception {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/admin/impersonate/{id}", "99").with(csrf()))
                .andExpect(content().string(containsString("User not found")));
    }

    // POST /api/admin/create
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUser_Success() throws Exception {
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        User newUser = new User();
        newUser.setId("200");
        newUser.setUsername("newUser");
        newUser.setEmail("newuser@example.com");
        newUser.setRole("USER");
        newUser.setPassword(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        String newUserJson = "{\"username\": \"newUser\", \"password\": \"" + rawPassword + "\", \"email\": \"newuser@example.com\"}";

        mockMvc.perform(post("/api/admin/create").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("200")))
                .andExpect(jsonPath("$.username", is("newUser")))
                .andExpect(jsonPath("$.email", is("newuser@example.com")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUser_Duplicate_returnsConflict() throws Exception {
        when(userRepository.findByUsername("duplicateUser")).thenReturn(Optional.of(new User()));
        String userJson = "{\"username\": \"duplicateUser\", \"password\": \"pass123\", \"email\": \"dup@example.com\"}";

        mockMvc.perform(post("/api/admin/create").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(content().string(containsString("Username already exists")));
    }
}

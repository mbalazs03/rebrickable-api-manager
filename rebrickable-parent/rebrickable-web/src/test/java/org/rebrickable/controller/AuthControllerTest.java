package org.rebrickable.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.rebrickable.User;
import org.rebrickable.repository.UserRepository;
import org.rebrickable.config.GlobalExceptionHandler;
import org.rebrickable.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Biztonsági filterek kikapcsolása a teszteléshez
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testRegister_Success() throws Exception {
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass123");
        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setUsername("testUser");
        savedUser.setEmail("test@example.com");
        savedUser.setRole("USER");
        savedUser.setPassword("encodedPass123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        String json = "{\"username\": \"testUser\", \"password\": \"pass123\", \"email\": \"test@example.com\"}";

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.username", is("testUser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    void testRegister_Failure() throws Exception {
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass123");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        String json = "{\"username\": \"testUser\", \"password\": \"pass123\", \"email\": \"test@example.com\"}";

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Database error")));
    }

    @Test
    void testLogin_Success() throws Exception {
        String username = "testUser";
        String password = "pass123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails dummyUserDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername(username)).thenReturn(dummyUserDetails);
        when(jwtUtil.generateToken(dummyUserDetails)).thenReturn("dummyToken");

        User authUser = new User();
        authUser.setId("1");
        authUser.setUsername(username);
        authUser.setEmail("test@example.com");
        authUser.setRole("ROLE_USER");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(authUser));

        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("dummyToken")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    void testLogin_AuthenticationFailure() throws Exception {
        String username = "testUser";
        String password = "pass123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(content().string(containsString("Authentication failed")));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        String username = "testUser";
        String password = "pass123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails dummyUserDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername(username)).thenReturn(dummyUserDetails);
        when(jwtUtil.generateToken(dummyUserDetails)).thenReturn("dummyToken");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("User not found")));
    }
}

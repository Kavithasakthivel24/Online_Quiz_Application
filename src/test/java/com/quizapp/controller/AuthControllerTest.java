package com.quizapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizapp.config.JwtFilter;
import com.quizapp.config.JwtUtil;
import com.quizapp.entity.User;
import com.quizapp.repository.*;
import com.quizapp.service.EmailService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ disables security
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ MOCK ALL REQUIRED DEPENDENCIES

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private ResultRepository resultRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // ============================
    // ✅ SIGNUP TEST
    // ============================
    @Test
    void testSignup() throws Exception {

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("12345");
        user.setEmail("test@gmail.com");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");

        mockMvc.perform(post("/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendRegistrationEmail(anyString(), anyString());
    }

    // ============================
    // ✅ LOGIN SUCCESS TEST
    // ============================
    @Test
    void testLoginSuccess() throws Exception {

        User user = new User();
        user.setUsername("admin");
        user.setPassword("encoded123");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("12345", "encoded123"))
                .thenReturn(true);

        mockMvc.perform(post("/doLogin")
                .contentType("application/x-www-form-urlencoded")
                .param("username", "admin")
                .param("password", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    // ============================
    // ❌ LOGIN FAIL TEST
    // ============================
    @Test
    void testLoginFail() throws Exception {

        when(userRepository.findByUsername("wrong"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/doLogin")
                .contentType("application/x-www-form-urlencoded")
                .param("username", "wrong")
                .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}
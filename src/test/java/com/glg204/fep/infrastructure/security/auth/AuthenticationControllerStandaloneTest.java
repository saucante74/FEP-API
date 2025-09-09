package com.glg204.fep.infrastructure.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glg204.fep.application.UserApplication.PasswordResetService;
import com.glg204.fep.application.UserApplication.UserNotificationService;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationControllerStandaloneTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtService = Mockito.mock(JwtService.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        UserNotificationService userNotificationService = Mockito.mock(UserNotificationService.class);
        PasswordResetService passwordResetService = Mockito.mock(PasswordResetService.class);
        objectMapper = new ObjectMapper();

        AuthenticationController controller = new AuthenticationController(
                userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                userNotificationService,
                passwordResetService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private RegisterRequest getSampleRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jdoe");
        request.setEmail("jdoe@example.com");
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setPassword("password123");
        request.setRole(UserRole.USER);
        return request;
    }

    private User getSampleUser() {
        return User.builder()
                .id(1L)
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword")
                .role(UserRole.USER)
                .status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = getSampleRegisterRequest();
        User user = getSampleUser();

        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("jdoe@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}

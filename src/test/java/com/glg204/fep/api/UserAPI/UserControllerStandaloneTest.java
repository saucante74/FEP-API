package com.glg204.fep.api.UserAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glg204.fep.application.UserApplication.UserRequestDTO;
import com.glg204.fep.application.UserApplication.UserResponseDTO;
import com.glg204.fep.application.UserApplication.UserService;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// UNT TESTS
class UserControllerStandaloneTest {

    private MockMvc mockMvc;
    private UserService userService;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        userRepository = Mockito.mock(UserRepository.class);
        objectMapper = new ObjectMapper();

        UserController userController = new UserController(userService, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private User getSampleUser() {
        return User.builder()
                .id(1L)
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .role(UserRole.USER)
                .status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = List.of(getSampleUser(), getSampleUser());
        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("jdoe@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jdoe@example.com")
                .role(UserRole.USER)
                .status(UserStatus.PENDING_VALIDATION)
                .build();

        Mockito.when(userService.getUserById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setUsername("jdoe");
        requestDTO.setEmail("jdoe@example.com");
        requestDTO.setFirstname("John");
        requestDTO.setLastname("Doe");
        requestDTO.setRole(UserRole.USER);
        requestDTO.setPassword("password");

        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jdoe@example.com")
                .role(UserRole.USER)
                .status(UserStatus.PENDING_VALIDATION)
                .build();

        Mockito.when(userService.updateUser(eq(1L), any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    void testPatchUser() throws Exception {
        User updatedUser = getSampleUser();
        updatedUser.setEmail("newemail@example.com");

        Mockito.when(userService.patchUser(eq(1L), any(Map.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"newemail@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newemail@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testValidateUser() throws Exception {
        mockMvc.perform(put("/api/users/1/validate"))
                .andExpect(status().isNoContent());
    }
}


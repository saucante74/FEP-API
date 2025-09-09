package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserResponseDTOTest {

    @Test
    void shouldMapUserToUserResponseDTOCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Alice");
        user.setLastName("Dupont");
        user.setEmail("alice@example.com");
        user.setRole(UserRole.ADMIN);
        user.setStatus(UserStatus.VALIDATED);

        UserResponseDTO dto = UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRole(), dto.getRole());
        assertEquals(user.getStatus(), dto.getStatus());
    }
}

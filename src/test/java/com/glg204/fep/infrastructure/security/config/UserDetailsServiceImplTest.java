package com.glg204.fep.infrastructure.security.config;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    private User getSampleUser() {
        return User.builder()
                .id(1L)
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .role(UserRole.USER)
                .status(UserStatus.VALIDATED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        User user = getSampleUser();
        Mockito.when(userRepository.findByEmail("jdoe@example.com")).thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername("jdoe@example.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jdoe@example.com");
        assertThat(result.getAuthorities()).hasSize(1);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        Mockito.when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown@example.com");
        });
    }
}

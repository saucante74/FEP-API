package com.glg204.fep.infrastructure.security.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationResponseTest {

    @Test
    void testBuilderAndGetters() {
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt-token")
                .firstName("John")
                .lastName("Doe")
                .email("jdoe@example.com")
                .role("USER")
                .build();

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getEmail()).isEqualTo("jdoe@example.com");
        assertThat(response.getRole()).isEqualTo("USER");
    }
}

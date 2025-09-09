package com.glg204.fep.infrastructure.security.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Unit tests
class AuthenticationRequestTest {

    @Test
    void testBuilderAndGetters() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("jdoe@example.com")
                .password("password123")
                .build();

        assertThat(request.getEmail()).isEqualTo("jdoe@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("jdoe@example.com");
        request.setPassword("password123");

        assertThat(request.getEmail()).isEqualTo("jdoe@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }
}

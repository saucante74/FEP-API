package com.glg204.fep.domain;

import com.glg204.fep.domain.UserDomain.PasswordResetToken;
import com.glg204.fep.domain.UserDomain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {

    @Test
    void testTokenIsExpired() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("abc123")
                .expiryDate(LocalDateTime.now().minusMinutes(1))
                .user(new User())
                .build();

        assertTrue(token.isExpired(), "Token should be expired.");
    }

    @Test
    void testTokenIsNotExpired() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("abc123")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .user(new User())
                .build();

        assertFalse(token.isExpired(), "Token shouldn't be expired.");
    }
}

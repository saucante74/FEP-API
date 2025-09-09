package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.PasswordResetToken;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.UserInfrastructure.PasswordResetTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @InjectMocks
    private PasswordResetService passwordResetService;

    public PasswordResetServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTokenWithExpiryDate() {
        User user = new User();
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PasswordResetToken token = passwordResetService.createToken(user);

        assertNotNull(token.getToken());
        assertNotNull(token.getExpiryDate());
        assertEquals(user, token.getUser());
        assertTrue(token.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsExpired() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiryDate(LocalDateTime.now().minusMinutes(1));

        when(tokenRepository.findByToken("expired")).thenReturn(Optional.of(token));

        assertThrows(IllegalArgumentException.class, () -> passwordResetService.validateToken("expired"));
    }
}

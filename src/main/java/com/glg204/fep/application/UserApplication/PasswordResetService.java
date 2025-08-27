package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.PasswordResetToken;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.UserInfrastructure.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .expiryDate(expiryDate)
                .user(user)
                .build();

        return tokenRepository.save(resetToken);
    }

    public PasswordResetToken validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token expired");
        }

        return resetToken;
    }

    @Transactional
    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }
}

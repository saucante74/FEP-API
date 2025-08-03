package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setStatus(UserStatus.PENDING_VALIDATION);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void validateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(UserStatus.VALIDATED);
        userRepository.save(user);
//        mailService.sendValidationEmail(user);
    }
}

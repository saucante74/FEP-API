package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import jakarta.transaction.Transactional;
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

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return this.toDto(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        System.out.println("DTO reçu : " + dto);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setEmail(dto.getEmail());

        userRepository.save(user);

        return this.toDto(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void validateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(UserStatus.VALIDATED);
        userRepository.save(user);
//        mailService.sendValidationEmail(user);
    }

    private UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setEmail(user.getEmail());

        return dto;
    }

}

package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

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
        user.setId(dto.getId());
        user.setLastName(dto.getLastname());
        user.setFirstName(dto.getFirstname());
        user.setEmail(dto.getEmail());

        userRepository.save(user);

        return this.toDto(user);
    }

    public User patchUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElseThrow();

        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("username")) {
            user.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("firstname")) {
            user.setFirstName((String) updates.get("firstname"));
        }
        if (updates.containsKey("lastname")) {
            user.setLastName((String) updates.get("lastname"));
        }
        if (updates.containsKey("role")) {
            user.setRole(UserRole.valueOf((String) updates.get("role")));
        }
        if (updates.containsKey("password")) {
            user.setPassword(passwordEncoder.encode((String) updates.get("password")));
        }

        return userRepository.save(user);
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

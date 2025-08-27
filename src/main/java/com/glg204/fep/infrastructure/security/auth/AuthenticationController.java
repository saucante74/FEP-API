package com.glg204.fep.infrastructure.security.auth;

import com.glg204.fep.application.UserApplication.PasswordResetService;
import com.glg204.fep.application.UserApplication.UserNotificationService;
import com.glg204.fep.domain.UserDomain.PasswordResetToken;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserStatus;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.security.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserNotificationService userNotificationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("REGISTER endpoint hit");

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already in use");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already in use");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .lastName(request.getLastname())
                .firstName(request.getFirstname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        userNotificationService.sendRegistrationMail(user);

        var jwt = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var jwt = jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @PostMapping("/reset-password-request")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        PasswordResetToken resetToken = passwordResetService.createToken(user);
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken.getToken();

        userNotificationService.sendPasswordResetMail(user, resetLink);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetService.validateToken(request.getToken());

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordResetService.deleteToken(request.getToken());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<Void> validateUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setStatus(UserStatus.VALIDATED);
        userRepository.save(user);

        userNotificationService.sendAccountValidatedMail(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);

        userNotificationService.sendAccountBlockedMail(user);
        return ResponseEntity.ok().build();
    }
}


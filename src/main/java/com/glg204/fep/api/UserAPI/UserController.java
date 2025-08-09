package com.glg204.fep.api.UserAPI;

import com.glg204.fep.application.UserApplication.UserResponseDTO;
import com.glg204.fep.application.UserApplication.UserService;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
//        return ResponseEntity.ok(userService.updateUser(id, userDTO));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }

    // VALIDATE
    @PutMapping("/{id}/validate")
    public ResponseEntity<Void> validateUser(@PathVariable Long id) {
        userService.validateUser(id);
        return ResponseEntity.noContent().build();
    }
}

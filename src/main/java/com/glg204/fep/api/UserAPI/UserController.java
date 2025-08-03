package com.glg204.fep.api.UserAPI;

import com.glg204.fep.application.UserApplication.UserDTO;
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

    @PutMapping("/{id}/validate")
    public void validate(@PathVariable Long id) {
        userService.validateUser(id);
    }
}

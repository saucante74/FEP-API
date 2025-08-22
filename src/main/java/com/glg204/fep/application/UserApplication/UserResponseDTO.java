package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private UserStatus status;
}

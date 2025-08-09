package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
}

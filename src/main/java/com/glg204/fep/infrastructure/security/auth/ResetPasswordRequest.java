package com.glg204.fep.infrastructure.security.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}

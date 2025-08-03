package com.glg204.fep.domain.UserDomain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    USER,
    ADMIN,
    BORROWER,
    LENDER;

    @Override
    public String getAuthority() {
        return name();
    }
}


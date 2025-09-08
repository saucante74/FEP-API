package com.glg204.fep.domain.UserDomain;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserAuthorities() {
        User user = User.builder()
                .email("test@example.com")
                .role(UserRole.ADMIN)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testUserUsernameReturnsEmail() {
        User user = User.builder()
                .email("user@example.com")
                .username("user123")
                .build();

        assertEquals("user@example.com", user.getUsername());
    }

    @Test
    void testUserAccountFlags() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserRoleAuthority() {
        assertEquals("USER", UserRole.USER.getAuthority());
        assertEquals("ADMIN", UserRole.ADMIN.getAuthority());
        assertEquals("BORROWER", UserRole.BORROWER.getAuthority());
        assertEquals("LENDER", UserRole.LENDER.getAuthority());
    }
}

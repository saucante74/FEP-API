package com.glg204.fep.infrastructure.UserInfrastructure;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findByStatus(UserStatus status);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}


package com.example.UserAuthenticationAndRoleManagement.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByFirebaseUid(String firebaseUid);
    Optional<User> findByEmail(String email);
}

package com.example.UserAuthenticationAndRoleManagement.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByName(@Param("keyword") String keyword);

    boolean existsByEmail(String email);
    Optional<User> findByFirebaseUid(String firebaseUid);
    Optional<User> findByEmail(String email);
}

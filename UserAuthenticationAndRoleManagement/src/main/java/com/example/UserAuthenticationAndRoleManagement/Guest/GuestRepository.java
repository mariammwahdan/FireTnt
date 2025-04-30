package com.example.UserAuthenticationAndRoleManagement.Guest;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GuestRepository extends JpaRepository<User, Long> {
    // Optional: custom queries for hosts
}
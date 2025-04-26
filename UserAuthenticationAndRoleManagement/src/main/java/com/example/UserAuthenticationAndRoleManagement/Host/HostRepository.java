package com.example.UserAuthenticationAndRoleManagement.Host;

import com.example.UserAuthenticationAndRoleManagement.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<User, Long> {
    // Optional: custom queries for hosts
}
package com.example.UserAuthenticationAndRoleManagement.Admin.Repository;

import com.example.UserAuthenticationAndRoleManagement.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository {

    public interface UserRepository extends JpaRepository<User, Long> { }


}
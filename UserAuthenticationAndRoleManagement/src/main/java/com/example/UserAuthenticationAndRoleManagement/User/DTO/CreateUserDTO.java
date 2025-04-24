package com.example.UserAuthenticationAndRoleManagement.User.DTO;

import com.example.UserAuthenticationAndRoleManagement.User.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CreateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

}

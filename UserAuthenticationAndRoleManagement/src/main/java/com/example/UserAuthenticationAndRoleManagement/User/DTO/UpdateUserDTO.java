package com.example.UserAuthenticationAndRoleManagement.User.DTO;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isBanned;
}

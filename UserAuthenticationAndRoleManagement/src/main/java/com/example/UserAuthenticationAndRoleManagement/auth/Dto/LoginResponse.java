package com.example.UserAuthenticationAndRoleManagement.auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String   userFirebaseId;
    private String email;
    private String idToken;
    private String refreshToken;

    public String getuserFirebaseId() {
        return userFirebaseId;
    }

    public void setuserFirebaseId(String userFirebaseId) {
        this.userFirebaseId = userFirebaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }




}

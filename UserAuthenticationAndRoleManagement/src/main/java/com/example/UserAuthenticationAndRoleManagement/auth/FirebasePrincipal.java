package com.example.UserAuthenticationAndRoleManagement.auth;


import lombok.Getter;

@Getter

public class FirebasePrincipal {

    private final String uid;
    private final String email;

    public FirebasePrincipal(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

}

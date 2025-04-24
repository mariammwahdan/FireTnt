package com.example.UserAuthenticationAndRoleManagement.auth.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FirebaseAuthResponse {
    @JsonProperty("idToken")
    private String idToken;

    @JsonProperty("localId")
    private String localId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("expiresIn")
    private String expiresIn;
}

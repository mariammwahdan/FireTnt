package com.example.UserAuthenticationAndRoleManagement.auth;

import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.example.UserAuthenticationAndRoleManagement.User.UserRepository;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.FirebaseAuthResponse;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginRequest;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//@Service
//public class FirebaseAuthenticationService {
//    private final UserRepository userRepo;
//    private final FirebaseAuth firebaseAuth;
//
//    public FirebaseAuthenticationService(
//            UserRepository userRepo,
//            FirebaseAuth firebaseAuth
//    ) {
//        this.userRepo     = userRepo;
//        this.firebaseAuth = firebaseAuth;
//    }
//
//    @Transactional
//    public User loginWithToken(String idToken) {
//        try {
//
//            FirebaseToken decoded = firebaseAuth.verifyIdToken(idToken);
//
//            String uid   = decoded.getUid();
//            String email = decoded.getEmail();
//
//            return userRepo.findByFirebaseUid(uid)
//                    .orElseGet(() -> {
//                        User u = new User();
//                        u.setFirebaseUid(uid);
//                        u.setEmail(email);
//                        u.setPassword("");
//                        return userRepo.save(u);
//                    });
//        } catch (FirebaseAuthException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED,
//                    "Invalid Firebase ID token",
//                    e
//            );
//        }
//    }
//
//    @Transactional
//    public void logout(String idToken) {
//        try {
//            FirebaseToken decoded = firebaseAuth.verifyIdToken(idToken);
//            firebaseAuth.revokeRefreshTokens(decoded.getUid());
//        } catch (FirebaseAuthException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Could not revoke tokens",
//                    e
//            );
//        }
//    }
//}

@Service
public class FirebaseAuthenticationService {
    private final UserRepository userRepo;
    private final WebClient webClient;
    private final String apiKey;
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthenticationService(
            UserRepository userRepo,
            WebClient firebaseAuthWebClient,
            @Value("${firebase.api-key}") String apiKey,
            FirebaseAuth firebaseAuth
    ) {
        this.userRepo     = userRepo;
        this.webClient    = firebaseAuthWebClient;
        this.apiKey       = apiKey;
        this.firebaseAuth = firebaseAuth;
    }

    public LoginResponse loginWithToken(String idToken) {
        try {
            FirebaseToken decoded = firebaseAuth.verifyIdToken(idToken);
            String firebaseUid = decoded.getUid();

            User u = userRepo.findByFirebaseUid(firebaseUid)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found"
                    ));

            return new LoginResponse(
                    u.getUserId(),
                    u.getEmail(),
                    idToken,     // optional: echo back
                    null         // no refreshToken, unless passed in
            );

        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid Firebase token",
                    e
            );
        }
    }

    @Transactional
    public LoginResponse loginWithEmail(LoginRequest req) {
        FirebaseAuthResponse resp = webClient.post()
                .uri(uri -> uri
                        .path("/accounts:signInWithPassword")
                        .queryParam("key", apiKey)
                        .build())
                .bodyValue(Map.of(
                        "email", req.getEmail(),
                        "password", req.getPassword(),
                        "returnSecureToken", true
                ))
                .retrieve()
                .onStatus(status -> status.value() == 400,
                        clientResp -> clientResp
                                .bodyToMono(ObjectNode.class)
                                .flatMap(body -> {
                                    String msg = body
                                            .path("error")
                                            .path("message")
                                            .asText("UNKNOWN");
                                    return Mono.error(
                                            new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg)
                                    );
                                })
                )
                .bodyToMono(FirebaseAuthResponse.class)
                .block();

        String uid = resp.getLocalId();
        User u = userRepo.findByFirebaseUid(uid)
                .orElseGet(() -> {
                    User n = new User();
                    n.setFirebaseUid(uid);
                    n.setEmail(resp.getEmail());
                    return userRepo.save(n);
                });

        return new LoginResponse(
                u.getUserId(),
                u.getEmail(),
                resp.getIdToken(),
                resp.getRefreshToken()
        );
    }

    @Transactional
    public void logout(String idToken) {
        try {
            FirebaseToken decoded = firebaseAuth.verifyIdToken(idToken);
            firebaseAuth.revokeRefreshTokens(decoded.getUid());
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not revoke tokens",
                    e
            );
        }
    }

    public String createSessionCookie(String idToken) {
        try {
            // session valid for 5 days
            long expiresIn = Duration.ofDays(5).toMillis();
            SessionCookieOptions options = SessionCookieOptions.builder()
                    .setExpiresIn(expiresIn)
                    .build();
            return firebaseAuth.createSessionCookie(idToken, options);
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Failed to create session cookie",
                    e
            );
        }
    }
    /**
     * Verify the session cookie, checking revocation status.
     * Returns the decoded FirebaseToken on success.
     */
    public FirebaseToken verifySession(String sessionCookie) {
        try {
            return firebaseAuth.verifySessionCookie(sessionCookie, /* checkRevoked= */ true);
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or revoked session cookie",
                    e
            );
        }
    }

    public void clearSession(String sessionCookie) {
        try {
            FirebaseToken decoded = firebaseAuth.verifySessionCookie(sessionCookie, true);
            firebaseAuth.revokeRefreshTokens(decoded.getUid());
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not clear session",
                    e
            );
        }
    }

    // add this method
    public String refreshIdToken(String refreshToken) {
        var form = new LinkedMultiValueMap<String,String>();
        form.add("grant_type","refresh_token");
        form.add("refresh_token", refreshToken);

        var resp = webClient.post()
                .uri(uri -> uri
                        .path("/token")
                        .queryParam("key", apiKey)
                        .build()
                )
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(com.fasterxml.jackson.databind.JsonNode.class)
                .block();

        return resp.get("id_token").asText();
    }


    public void assignAdminRole(String firebaseUid) throws FirebaseAuthException {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role","ADMIN");
        firebaseAuth.setCustomUserClaims(firebaseUid, claims);
    }


}
package com.example.UserAuthenticationAndRoleManagement.auth;


import com.example.UserAuthenticationAndRoleManagement.User.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter

public class FirebasePrincipal implements UserDetails {

    private final String uid;
    private final String email;
    private final Role role;


    public FirebasePrincipal(String uid, String email, Role role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }


//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
//    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }


    public String getPassword() {
        return null;  // Not needed for Firebase authentication
    }


    public String getUsername() {
        return email;  // Firebase email is used as username
    }


    public boolean isAccountNonExpired() {
        return true;
    }


    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public Role getRole() {
        return role;
    }
}

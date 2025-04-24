package com.example.UserAuthenticationAndRoleManagement.User;

import jakarta.persistence.*;
import lombok.Data;

import javax.management.Notification;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String firebaseUid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isBanned;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }



    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long id) { this.userId = id; }

    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }

    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String fn) { this.firstName = fn; }

    public String getLastName() { return lastName; }
    public void setLastName(String ln) { this.lastName = ln; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String pn) { this.phoneNumber = pn; }

    public boolean isBanned() { return isBanned; }
    public void setBanned(boolean b) { this.isBanned = b; }



}

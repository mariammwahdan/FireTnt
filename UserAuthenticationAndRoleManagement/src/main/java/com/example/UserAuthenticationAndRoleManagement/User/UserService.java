package com.example.UserAuthenticationAndRoleManagement.User;
//package com.example.UserAuthenticationAndRoleManagement.User;
//
//
//import com.example.UserAuthenticationAndRoleManagement.User.Client.NotificationClient;
//import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
//import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
//import com.example.UserAuthenticationAndRoleManagement.User.DTO.UpdateUserDTO;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//    private final NotificationClient notifClient;
//
//    public UserService(UserRepository userRepository, NotificationClient notifClient) {
//        this.userRepository = userRepository;
//        this.notifClient = notifClient;
//    }
//
//    @Cacheable("users")
//    public List<User> fetchAll() {
//        return userRepository.findAll();
//    }
//
//    @Cacheable(value = "users", key = "#id")
//    public User fetchById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    @CacheEvict(value = "users", allEntries = true)
//    public User createUser(CreateUserDTO dto) {
//        if (userRepository.existsByEmail(dto.getEmail())) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Email already in use"
//            );
//        }
//        User u = new User();
//        u.setEmail(dto.getEmail());
//        u.setPassword(dto.getPassword());
//        u.setFirstName(dto.getFirstName());
//        u.setLastName(dto.getLastName());
//        u.setPhoneNumber(dto.getPhoneNumber());
//        return userRepository.save(u);
//    }
//
//
//    @CacheEvict(value = "users", allEntries = true)
//    public User updateUser(Long id, UpdateUserDTO dto) {
//        User u = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        u.setEmail(dto.getEmail());
//        u.setPassword(dto.getPassword());
//        u.setFirstName(dto.getFirstName());
//        u.setLastName(dto.getLastName());
//        u.setPhoneNumber(dto.getPhoneNumber());
//        u.setBanned(dto.isBanned());
//        return userRepository.save(u);
//    }
//
//    @CacheEvict(value = "users", allEntries = true)
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//
//    public List<NotificationDto> getNotifications(Long userId) {
//        List<Long> ids = notifClient.fetchIdsByUser(userId);
//        return ids.stream()
//                .map(notifClient::fetchById)
//                .toList();
//    }
//
//
//
//
//}




import com.example.UserAuthenticationAndRoleManagement.User.Client.NotificationClient;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.UpdateUserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationClient notifClient;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();


    public UserService(UserRepository userRepository, NotificationClient notifClient) {
        this.userRepository = userRepository;
        this.notifClient = notifClient;

    }

    @Cacheable("users")
    public List<User> fetchAll() {
        return userRepository.findAll(Sort.by("userId"));
    }

    @Cacheable(value = "users", key = "#id")
    public User fetchById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

//    @CacheEvict(value = "users", allEntries = true)
//    @Transactional
//    public User createUser(CreateUserDTO dto) {
//        try {
//            var req = new UserRecord.CreateRequest()
//                    .setEmail(dto.getEmail())
//                    .setPassword(dto.getPassword());
//            var rec = auth.createUser(req);
//
//            var u = new User();
//            u.setFirebaseUid(rec.getUid());
//            u.setEmail(rec.getEmail());
//            u.setFirstName(dto.getFirstName());
//            u.setLastName(dto.getLastName());
//            u.setPhoneNumber(dto.getPhoneNumber());
//            u.setPassword(dto.getPassword());
//            return userRepository.save(u);
//
//        } catch (FirebaseAuthException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Firebase create failed",
//                    e
//            );
//        }
//    }

    public Long findUserIdByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid)
                .map(User::getUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        ));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User createUser(CreateUserDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email " + dto.getEmail() + " is already registered. Please use a different email."
            );
        }

        try {
            // 2) check Firebase
            auth.getUserByEmail(dto.getEmail());
            // if no exception thrown, user exists in Firebase
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email " + dto.getEmail() + " is already registered in Firebase"
            );

        } catch (FirebaseAuthException e) {
            // e.getAuthErrorCode()==USER_NOT_FOUND means it’s safe to create
            if (!"USER_NOT_FOUND".equals(e.getAuthErrorCode().name())) {
                // some other Firebase error
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to verify email in Firebase",
                        e
                );
            }
        }

        try {

            var req = new UserRecord.CreateRequest()
                    .setEmail(dto.getEmail())
                    .setPassword(dto.getPassword());
            var rec = auth.createUser(req);


            var u = new User();
            u.setFirebaseUid(rec.getUid());
            u.setEmail(rec.getEmail());
            u.setFirstName(dto.getFirstName());
            u.setLastName(dto.getLastName());
            u.setPhoneNumber(dto.getPhoneNumber());
            u.setPassword(dto.getPassword());
            u.setRole(dto.getRole());
            return userRepository.save(u);

        } catch (FirebaseAuthException e) {

            if ("EMAIL_ALREADY_EXISTS".equals(e.getAuthErrorCode().name())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email " + dto.getEmail() + " is in use on Firebase. Please choose another email.",
                        e
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create user on Firebase",
                    e
            );
        }
    }

//    @CacheEvict(value = "users", allEntries = true)
//    @Transactional
//    public User updateUser(Long id, UpdateUserDTO dto) {
//        var u = userRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found"
//                ));
//
//        try {
//            var req = new UserRecord.UpdateRequest(u.getFirebaseUid());
//            if (dto.getEmail() != null)    req.setEmail(dto.getEmail());
//            if (dto.getPassword() != null) req.setPassword(dto.getPassword());
//            auth.updateUser(req);
//
//        } catch (FirebaseAuthException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Firebase update failed",
//                    e
//            );
//        }
//
//        if (dto.getEmail() != null)       u.setEmail(dto.getEmail());
//        if (dto.getFirstName() != null)   u.setFirstName(dto.getFirstName());
//        if (dto.getLastName() != null)    u.setLastName(dto.getLastName());
//        if (dto.getPhoneNumber() != null) u.setPhoneNumber(dto.getPhoneNumber());
//        u.setBanned(dto.isBanned());
//
//        return userRepository.save(u);
//    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void deleteUser(Long id) {
        var u = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        try {
            auth.deleteUser(u.getFirebaseUid());
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Firebase delete failed",
                    e
            );
        }

        userRepository.deleteById(id);
    }

    public List<NotificationDto> getNotifications(Long userId) {
        var ids = notifClient.fetchIdsByUser(userId);
        return ids.stream()
                .map(notifClient::fetchById)
                .toList();
    }


}

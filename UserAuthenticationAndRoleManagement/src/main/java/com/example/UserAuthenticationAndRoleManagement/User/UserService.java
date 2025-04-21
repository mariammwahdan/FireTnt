package com.example.UserAuthenticationAndRoleManagement.User;


import com.example.UserAuthenticationAndRoleManagement.User.Client.NotificationClient;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.UpdateUserDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationClient notifClient;

    public UserService(UserRepository userRepository, NotificationClient notifClient) {
        this.userRepository = userRepository;
        this.notifClient = notifClient;
    }

    @Cacheable("users")
    public List<User> fetchAll() {
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#id")
    public User fetchById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User createUser(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already in use"
            );
        }
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhoneNumber(dto.getPhoneNumber());
        return userRepository.save(u);
    }


    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(Long id, UpdateUserDTO dto) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhoneNumber(dto.getPhoneNumber());
        u.setBanned(dto.isBanned());
        return userRepository.save(u);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<NotificationDto> getNotifications(Long userId) {
        List<Long> ids = notifClient.fetchIdsByUser(userId);
        return ids.stream()
                .map(notifClient::fetchById)
                .toList();
    }




}

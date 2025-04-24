package com.example.UserAuthenticationAndRoleManagement.User;




import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.UpdateUserDTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<User> getAll() {
        return svc.fetchAll();
    }

//    @GetMapping("/{id}")
//    public User getById(@PathVariable Long id) {
//        return svc.fetchById(id);
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody CreateUserDTO dto) {
        return svc.createUser(dto);
    }

//    @PutMapping("/{id}")
//    public User update(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
//        return svc.updateUser(id, dto);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        svc.deleteUser(id);
    }

    @GetMapping("/{id}/notifications")
    public List<NotificationDto> notifications(@PathVariable Long id) {
        return svc.getNotifications(id);
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal) {
        User u = svc.findByEmail(principal.getName());
        switch(u.getRole()) {
            case ADMIN: return "redirect:/admin/home";
            case HOST:  return "redirect:/host/home";
            default:    return "redirect:/guest/home";
        }
    }
}

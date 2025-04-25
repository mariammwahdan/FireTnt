package com.example.UserAuthenticationAndRoleManagement.User;




import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    public String dashboard(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof FirebasePrincipal firebasePrincipal) {
            String email = firebasePrincipal.getEmail();
            User user = svc.findByEmail(email);
            String roleName =user.getRole().name();
//            switch (user.getRole()) {
//                case ADMIN: return "home";
//                case HOST: return "redirect:/host/home";
//                default: return "redirect:/guest/home";
//            }
            model.addAttribute("role", roleName);
            return "home";
        }

        throw new IllegalStateException("Invalid principal type");
    }


}

package com.example.UserAuthenticationAndRoleManagement.auth;

import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new CreateUserDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(
            @Valid @ModelAttribute("signupRequest") CreateUserDTO dto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "signup"; // Form validation failed
        }

        try {
            userService.createUser(dto);
            //model.addAttribute("loginRequest",new LoginRequest());
            return "redirect:/login"; // Or wherever you want to send them
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
}

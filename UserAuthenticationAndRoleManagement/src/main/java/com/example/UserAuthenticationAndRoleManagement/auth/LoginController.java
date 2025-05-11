package com.example.UserAuthenticationAndRoleManagement.auth;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final FirebaseAuthenticationService authSvc;
    public LoginController(FirebaseAuthenticationService authSvc) {
        this.authSvc = authSvc;
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }


}

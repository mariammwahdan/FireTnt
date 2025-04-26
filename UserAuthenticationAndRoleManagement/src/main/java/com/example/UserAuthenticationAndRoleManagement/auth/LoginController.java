package com.example.UserAuthenticationAndRoleManagement.auth;

import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginRequest;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

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


//    @PostMapping("/login")
//    public String handleLogin(
//            @ModelAttribute("loginRequest") LoginRequest request,
//            BindingResult result,
//            HttpServletResponse response,
//            Model model
//    ) {
//        if (result.hasErrors()) {
//            return "login";
//        }
//
//        try {
//            LoginResponse login = authSvc.loginWithEmail(request);
//
//            String session = authSvc.createSessionCookie(login.getIdToken());
//
//            ResponseCookie sessionCookie = ResponseCookie.from("SESSION", session)
//                    .httpOnly(true)
//                    .secure(true)
//                    .path("/")
//                    .maxAge(Duration.ofDays(5))
//                    .sameSite("Strict")
//                    .build();
//            response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());
//
//            return "redirect:api/users/dashboard";
//
//        } catch (ResponseStatusException e) {
//            model.addAttribute("error", "Invalid email or password.");
//            return "login";
//        }
//    }
}

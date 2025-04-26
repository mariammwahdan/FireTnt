package com.example.UserAuthenticationAndRoleManagement.auth;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginRequest;
import com.example.UserAuthenticationAndRoleManagement.auth.Dto.LoginResponse;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;


@Controller
@RequestMapping("/api/auth")
public class AuthController {
    private final FirebaseAuthenticationService authSvc;

    public AuthController(FirebaseAuthenticationService authSvc) {
        this.authSvc = authSvc;
    }

    // helper to set cookie
    private void setCookie(HttpServletResponse res,
                           String name,
                           String value,
                           int maxAgeSec) {
        Cookie c = new Cookie(name, value);
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setPath("/");
        c.setMaxAge(maxAgeSec);
        res.addCookie(c);
    }

    // helper to get cookie
    private String getCookie(HttpServletRequest req,
                             String name) {
        Cookie c = WebUtils.getCookie(req, name);
        return c != null ? c.getValue() : null;
    }
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest req) {
//        return authSvc.loginWithEmail(req);
//    }

//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest req,
//                               HttpServletResponse res) {
//        // 1. authenticate and get idToken+refreshToken
//        LoginResponse login = authSvc.loginWithEmail(req);
//
//        // 2. create session cookie (5 days)
//        String session = authSvc.createSessionCookie(login.getIdToken());
//        setCookie(res, "SESSION", session, 5 * 24 * 3600);
//
//        // 3. store refresh token (30 days)
//        setCookie(res, "REFRESH_TOKEN", login.getRefreshToken(),
//                30 * 24 * 3600);
//
//        return login;
//    }

//@PostMapping("/login")
//public String handleLogin(
//        @ModelAttribute("loginRequest") LoginRequest request,
//        BindingResult result,
//        HttpServletResponse response,
//        Model model
//) {
//    if (result.hasErrors()) {
//        return "login";
//    }
//
//    try {
//        LoginResponse login = authSvc.loginWithEmail(request);
//
//        String session = authSvc.createSessionCookie(login.getIdToken());
//
//        ResponseCookie sessionCookie = ResponseCookie.from("SESSION", session)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(Duration.ofDays(5))
//                .sameSite("Strict")
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());
//
//        return "redirect:/api/users/dashboard";
//
//    } catch (ResponseStatusException e) {
//        model.addAttribute("error", "Invalid email or password.");
//        return "login";
//    }
//}

//    @PostMapping("/login")
//    public ResponseEntity<?> handleLogin(
//            @ModelAttribute("loginRequest") LoginRequest request, // For HTML form submission
//            BindingResult result,
//            HttpServletRequest requestHttp, // To check the accept header for response type
//            HttpServletResponse response,
//            Model model
//    ) {
//        // Check if the request expects HTML or JSON
//        boolean isJsonRequest = requestHttp.getHeader("Accept") != null && requestHttp.getHeader("Accept").contains("application/json");
//
//        if (result.hasErrors()) {
//            return isJsonRequest ? ResponseEntity.badRequest().body("Invalid input data.") : "login";
//        }
//
//        try {
//            LoginResponse login = authSvc.loginWithEmail(request);
//
//            // Create session cookie
//            String session = authSvc.createSessionCookie(login.getIdToken());
//            ResponseCookie sessionCookie = ResponseCookie.from("SESSION", session)
//                    .httpOnly(true)
//                    .secure(true)
//                    .path("/")
//                    .maxAge(Duration.ofDays(5))
//                    .sameSite("Strict")
//                    .build();
//            response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());
//
//            if (isJsonRequest) {
//                // Return LoginResponse as JSON
//                return ResponseEntity.ok(login);
//            } else {
//                // Return HTML view redirection for web users
//                return "redirect:/api/users/dashboard";
//            }
//
//        } catch (ResponseStatusException e) {
//            if (isJsonRequest) {
//                // Return JSON error message for API users
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password.");
//            } else {
//                model.addAttribute("error", "Invalid email or password.");
//                return "login"; // Return the login view for web users
//            }
//        }
//    }

    @PostMapping("/login")
    public String handleLogin(
            @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult result,
            HttpServletResponse response,
            Model model
    ) {
        if (result.hasErrors()) {
            return "login"; // HTML view for login form with error
        }

        try {
            LoginResponse login = authSvc.loginWithEmail(request);

            String session = authSvc.createSessionCookie(login.getIdToken());

            ResponseCookie sessionCookie = ResponseCookie.from("SESSION", session)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(5))
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());

            return "redirect:/api/users/dashboard"; // Redirect after successful login

        } catch (ResponseStatusException e) {
            model.addAttribute("error", "Invalid email or password.");
            return "login"; // Return the login page with error message
        }
    }

    @PostMapping("/login/json")  // JSON response handler
    @ResponseBody  // Returns JSON directly without rendering a view
    public ResponseEntity<?> handleLoginJson(@RequestBody LoginRequest request) {
        try {
            LoginResponse login = authSvc.loginWithEmail(request);
            return ResponseEntity.ok(login);  // Return LoginResponse as JSON

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }




    @PostMapping("/refresh")
    public void refresh(HttpServletRequest req,
                        HttpServletResponse res) throws IOException {
        String refreshToken = getCookie(req, "REFRESH_TOKEN");
        if (refreshToken == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "No refresh token");
        }

        // 4. swap for new idToken
        String newIdToken = authSvc.refreshIdToken(refreshToken);

        // 5. issue new session cookie
        String session = authSvc.createSessionCookie(newIdToken);
        setCookie(res, "SESSION", session, 5 * 24 * 3600);
    }
//    @PostMapping("/logout")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void logout(@RequestBody Map<String,String> body) {
//        String idToken = body.get("idToken");
//        authSvc.logout(idToken);
//    }
    // existing login returns idToken+refresh, or you can skip if you go pure session
    @PostMapping("/sessionLogin")
    public LoginResponse sessionLogin(
            @RequestBody Map<String,String> body,
            HttpServletResponse response
    ) {
        String idToken = body.get("idToken");
        String refreshToken = body.get("refreshToken"); // include this if needed

        LoginResponse login = authSvc.loginWithToken(idToken); // returns user info, token, refresh

        String sessionCookie = authSvc.createSessionCookie(idToken);

        ResponseCookie cookie = ResponseCookie.from("SESSION", sessionCookie)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(5))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        ResponseCookie refresh = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());

        return login;
    }

    @PostMapping("/sessionLogout")
    public void sessionLogout(
            @CookieValue(name="SESSION", required=false) String sessionCookie,
            HttpServletResponse response
    ) {
        // optional: authSvc.clearSession(sessionCookie);
        ResponseCookie cleared = ResponseCookie.from("SESSION","")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cleared.toString());
    }

    @PostMapping("/assignAdminRole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignAdminRole(@RequestBody Map<String,String> body) throws FirebaseAuthException {
        String firebaseUid = body.get("firebaseUid");
        if (firebaseUid == null || firebaseUid.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "firebaseUid required");
        }
        authSvc.assignAdminRole(firebaseUid);
    }
}
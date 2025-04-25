package com.example.UserAuthenticationAndRoleManagement.FireBaseConfig;

import com.example.UserAuthenticationAndRoleManagement.auth.FirebaseAuthenticationService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final FirebaseAuthenticationService authSvc;

    public SecurityConfig(FirebaseAuthenticationService authSvc){
        this.authSvc = authSvc;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(sessionCookieFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/signup", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/signup", "/api/auth/login").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

//    @Bean
//    public Filter sessionCookieFilter() {
//        return new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest req,
//                                            HttpServletResponse res,
//                                            FilterChain chain)
//                    throws ServletException, IOException {
//                Cookie c = WebUtils.getCookie(req,"SESSION");
//                if (c != null) {
//                    FirebaseToken token = authSvc.verifySession(c.getValue());
//                    // build an Authentication and set into SecurityContext
//                    // e.g. UsernamePasswordAuthenticationToken principal
//                }
//                chain.doFilter(req,res);
//            }
//        };
//    }

    @Bean
    public OncePerRequestFilter sessionCookieFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    HttpServletRequest req,
                    HttpServletResponse res,
                    FilterChain chain
            ) throws ServletException, IOException {
                Cookie c = WebUtils.getCookie(req, "SESSION");
                if (c != null) {
                    FirebaseToken token = authSvc.verifySession(c.getValue());
                    String role = (String) token.getClaims().get("role");
                    List<GrantedAuthority> auths = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                    );
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            new FirebasePrincipal(token.getUid(), token.getEmail()),
                            null,
                            auths
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                chain.doFilter(req, res);
            }
        };
    }
}


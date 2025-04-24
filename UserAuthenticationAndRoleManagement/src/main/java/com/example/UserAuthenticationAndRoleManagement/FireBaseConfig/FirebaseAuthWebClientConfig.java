package com.example.UserAuthenticationAndRoleManagement.FireBaseConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FirebaseAuthWebClientConfig {
    @Bean
    public WebClient firebaseAuthWebClient() {
        return WebClient.builder()
                .baseUrl("https://identitytoolkit.googleapis.com/v1")
                .build();
    }
}

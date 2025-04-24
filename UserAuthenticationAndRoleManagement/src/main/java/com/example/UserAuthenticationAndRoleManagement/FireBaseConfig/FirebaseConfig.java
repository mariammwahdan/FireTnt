package com.example.UserAuthenticationAndRoleManagement.FireBaseConfig;

//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.auth.FirebaseAuth;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;

//@Component
//public class FirebaseConfig {
//    @PostConstruct
//    public void init() throws Exception {
//        InputStream serviceAccount =
//                getClass()
//                        .getResourceAsStream("/fire-tnt-firebase-adminsdk-fbsvc-e8464eacd5.json");
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        if (FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options);
//        }
//    }
//}

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//@Configuration
//public class FirebaseConfig {
//
//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        try (InputStream in = getClass()
//                .getResourceAsStream("/fire-tnt-firebase-adminsdk-fbsvc-e8464eacd5.json")) {
//            FirebaseOptions opts = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(in))
//                    .build();
//            return FirebaseApp.initializeApp(opts);
//        }
//    }
//
//    @Bean
//    public FirebaseAuth firebaseAuth(FirebaseApp app) {
//        return FirebaseAuth.getInstance(app);
//    }
//}


@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Read service account path from environment variable
        String credPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credPath == null || credPath.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable GOOGLE_APPLICATION_CREDENTIALS must be set to the service account JSON path"
            );
        }

        try (FileInputStream serviceAccount = new FileInputStream(credPath)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Initialize or return existing instance
            return FirebaseApp.getApps().isEmpty()
                    ? FirebaseApp.initializeApp(options)
                    : FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp app) {
        return FirebaseAuth.getInstance(app);
    }
}

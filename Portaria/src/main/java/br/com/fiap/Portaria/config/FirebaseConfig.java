package br.com.fiap.Portaria.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    // Lê de app.firebase.credentials (que por sua vez lê de ${FIREBASE_CREDENTIALS})
    @Value("${app.firebase.credentials:}")
    private String firebaseCredentials;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credentials;

            if (firebaseCredentials != null && !firebaseCredentials.isBlank()) {
                // Produção: lê da variável de ambiente
                credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(firebaseCredentials.getBytes())
                );
            } else {
                // Desenvolvimento local: lê do arquivo
                credentials = GoogleCredentials.fromStream(
                        getClass().getResourceAsStream("/firebase-serviceaccount.json")
                );
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
}

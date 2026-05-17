package br.com.fiap.Portaria.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CREDENTIALS}")
    private String firebaseCredentials;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credentials;

            if (firebaseCredentials != null && !firebaseCredentials.isEmpty()) {
                // lê das variáveis de ambiente (produção/Azure)
                credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(firebaseCredentials.getBytes())
                );
            } else {
                // lê do arquivo local (desenvolvimento)
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
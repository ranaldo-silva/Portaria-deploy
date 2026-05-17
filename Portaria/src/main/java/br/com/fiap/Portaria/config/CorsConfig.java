package br.com.fiap.Portaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Aceita qualquer origem (compatível com Expo Web e React Native Web)
        config.addAllowedOriginPattern("*");

        // Métodos permitidos — inclui OPTIONS para preflight
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers permitidos
        config.setAllowedHeaders(List.of("*"));

        // Expõe o Authorization no response (necessário para apps mobile/web)
        config.setExposedHeaders(List.of("Authorization"));

        // Sem credentials globais (incompatível com allowedOriginPattern("*"))
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

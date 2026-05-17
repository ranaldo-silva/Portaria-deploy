package br.com.fiap.Portaria.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Portaria API",
                version = "v2",
                description = "API para gerenciamento de portaria de condomínio",
                contact = @Contact(name = "Portaria", email = "rm560179@fiap.com.br")
        ),
        servers = {
                @Server(url = "/", description = "Servidor atual (local ou produção)")
        },
        tags = {@Tag(name = "Portaria", description = "Sistema de entregas de encomendas")}
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "Firebase Token"
)
public class SwaggerConfig {
}

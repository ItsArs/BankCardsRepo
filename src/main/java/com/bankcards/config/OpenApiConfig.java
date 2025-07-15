package com.bankcards.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Bankcards API",
                version = "1.0",
                description = "Admin name and password for testing: 'Admin':'admin'. " +
                        "User name and password for testing: 'NewUser':'NewUser'"
        ),
        servers = @Server(
                description = "Localhost",
                url = "http://localhost:8080"
        )
)
@SecurityScheme(
        name = "BearerAuth",
        description = "Jwt spring security",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

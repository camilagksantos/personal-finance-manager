package com.camilagksantos.finance.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Personal Finance Manager API")
                        .description("API for personal finance management with accounts, transactions and reports.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Camila Santos")
                                .email("camilagksantos@email.com")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("oauth2"))
                .components(new Components()
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl("http://localhost:8080/oauth2/authorize")
                                                .tokenUrl("http://localhost:8080/oauth2/token")
                                                .scopes(new Scopes()
                                                        .addString("finance.read", "Read financial data")
                                                        .addString("finance.write", "Write financial data")
                                                        .addString("openid", "OpenID Connect")
                                                        .addString("profile", "User profile information")
                                                )
                                        )
                                )
                        )
                );
    }
}
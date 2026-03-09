package com.camilagksantos.finance.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
                );
    }
}
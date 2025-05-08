package com.example.subscriptionservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Subscription Service API",
                version = "1.0",
                description = "API for users and subscription management"
        )
)
public class SwaggerConfig {
}
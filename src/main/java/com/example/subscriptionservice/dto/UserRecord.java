package com.example.subscriptionservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecord(
        @Schema(
                description = "Unique user ID",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @NotBlank(message = "Name required")
        @Size(max = 100, message = "The name must not exceed 100 characters")
        @Schema(
                description = "Full username",
                example = "John Doe",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,

        @Email(message = "Invalid email")
        @NotBlank(message = "Email required")
        @Schema(
                description = "User email",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email
) {
}
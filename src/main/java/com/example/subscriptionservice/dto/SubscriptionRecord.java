package com.example.subscriptionservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubscriptionRecord(
        @Schema(
                description = "Unique subscription ID",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @NotBlank(message = "Service name is required")
        @Schema(
                description = "Subscription service name",
                example = "Netflix",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String serviceName,

        @NotNull(message = "Cost is required")
        @Positive(message = "The monthly fee must be positive")
        @Schema(
                description = "Monthly subscription fee",
                example = "9.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Double monthlyFee,

        @Schema(
                description = "User ID of subscription owner",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long userId
) {
}
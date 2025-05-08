package com.example.subscriptionservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The entity of subscription to a service")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique subscription ID", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Service name is required")
    @Schema(description = "Service name", example = "Netflix", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceName;

    @Column(nullable = false)
    @NotNull(message = "Monthly fee is required")
    @Positive(message = "The fee must be positive")
    @Schema(description = "Monthly subscription fee", example = "9.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double monthlyFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "User who owns the subscription", hidden = true)
    private User user;
}
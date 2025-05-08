package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.dto.SubscriptionRecord;
import com.example.subscriptionservice.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users/{userId}/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscription Management", description = "API for subscription management")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(
            summary = "Add subscription",
            description = "\n" +
                    "Creates a new subscription for the specified user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<SubscriptionRecord> addSubscription(
            @Parameter(description = "ID пользователя", example = "1", required = true)
            @PathVariable Long userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for creating a subscription",
                    required = true
            )
            @Valid @RequestBody SubscriptionRecord request
    ) {
        log.info("Adding subscription for user ID: {}", userId);
        SubscriptionRecord created = subscriptionService.addSubscription(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(
            summary = "Get user subscriptions",
            description = "Returns a list of all subscriptions of the specified user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of subscriptions"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<List<SubscriptionRecord>> getUserSubscriptions(
            @Parameter(description = "ID пользователя", example = "1", required = true)
            @PathVariable Long userId
    ) {
        log.info("Fetching subscriptions for user ID: {}", userId);
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    @DeleteMapping("/{subscriptionId}")
    @Operation(
            summary = "Delete subscription",
            description = "Delete subscription for user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Subscription deleted"),
                    @ApiResponse(responseCode = "404", description = "Subscription not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(
            @Parameter(description = "Subscription id", example = "1", required = true)
            @PathVariable Long subscriptionId
    ) {
        log.info("Deleting subscription with ID: {}", subscriptionId);
        subscriptionService.deleteSubscription(subscriptionId);
    }
}
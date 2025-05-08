package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.dto.UserRecord;
import com.example.subscriptionservice.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API for user management")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Create user",
            description = "Create user in system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created"),
                    @ApiResponse(responseCode = "400", description = "Invalid data")
            }
    )
    public ResponseEntity<UserRecord> createUser(
            @Valid @RequestBody UserRecord request
    ) {
        log.info("Creating user with email: {}", request.email());
        UserRecord createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find user by ID",
            description = "Get user's details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    public ResponseEntity<UserRecord> getUser(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        log.debug("Fetching user with ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Returns list of all users"
    )
    public ResponseEntity<List<UserRecord>> getAllUsers() {
        log.debug("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user details",
            description = "Updates information about an existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Data updated"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<UserRecord> updateUser(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id,

            @Valid @RequestBody UserRecord request
    ) {
        log.info("Updating user with ID: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Removes a user from the system",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
    }
}
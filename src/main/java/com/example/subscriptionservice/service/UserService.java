package com.example.subscriptionservice.service;

import com.example.subscriptionservice.dto.UserRecord;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.exception.UserAlreadyExistsException;
import com.example.subscriptionservice.model.User;
import com.example.subscriptionservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserRecord createUser(UserRecord request) {
        log.info("Creating user with email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Attempt to create duplicate user with email: {}", request.email());
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());

        User savedUser = userRepository.save(user);
        log.debug("User created with ID: {}", savedUser.getId());

        return toRecord(savedUser);
    }

    public UserRecord getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new NotFoundException("User not found");
                });
        return toRecord(user);
    }

    public List<UserRecord> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::toRecord)
                .toList();
    }

    @Transactional
    public UserRecord updateUser(Long id, UserRecord request) {
        log.info("Updating user with ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found for update with ID: {}", id);
                    return new NotFoundException("User not found");
                });

        if (!existingUser.getEmail().equals(request.email())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new UserAlreadyExistsException("Email already in use");
            }
        }

        existingUser.setName(request.name());
        existingUser.setEmail(request.email());

        User updatedUser = userRepository.save(existingUser);
        log.debug("User with ID {} updated", id);
        return toRecord(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.error("User not found for deletion with ID: {}", id);
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.debug("User with ID {} deleted", id);
    }

    private UserRecord toRecord(User user) {
        return new UserRecord(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
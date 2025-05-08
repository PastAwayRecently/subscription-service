package com.example.subscriptionservice.service;

import com.example.subscriptionservice.dto.SubscriptionRecord;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.model.Subscription;
import com.example.subscriptionservice.model.User;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import com.example.subscriptionservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubscriptionRecord addSubscription(Long userId, SubscriptionRecord request) {
        log.info("Adding subscription for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new NotFoundException("User not found");
                });

        Subscription subscription = new Subscription();
        subscription.setServiceName(request.serviceName());
        subscription.setMonthlyFee(request.monthlyFee());
        subscription.setUser(user);

        Subscription saved = subscriptionRepository.save(subscription);
        log.debug("Subscription created with ID: {}", saved.getId());

        return toRecord(saved);
    }

    public List<SubscriptionRecord> getUserSubscriptions(Long userId) {
        log.info("Fetching subscriptions for user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found");
        }

        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::toRecord)
                .toList();
    }

    @Transactional
    public void deleteSubscription(Long subscriptionId) {
        log.info("Deleting subscription with ID: {}", subscriptionId);

        if (!subscriptionRepository.existsById(subscriptionId)) {
            log.error("Subscription not found with ID: {}", subscriptionId);
            throw new NotFoundException("Subscription not found");
        }

        subscriptionRepository.deleteById(subscriptionId);
        log.debug("Subscription with ID {} deleted", subscriptionId);
    }

    private SubscriptionRecord toRecord(Subscription subscription) {
        return new SubscriptionRecord(
                subscription.getId(),
                subscription.getServiceName(),
                subscription.getMonthlyFee(),
                subscription.getUser().getId()
        );
    }
}
package com.strategic.ludo.strategicLudo.config;

import Services.SessionService;
import Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CleanupScheduler {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    // Run every hour
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void cleanupEmptySessions() {
        sessionService.deleteEmptySessions();
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cleanupInactiveGuestUsers() {
        // Delete guest users that have been inactive for 4 hours
        userService.deleteInactiveGuestUsers(4);
    }
}
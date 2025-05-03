package com.strategic.ludo.strategicLudo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Apply CORS to all endpoints under /api
                        .allowedOrigins("http://localhost:8081", "https://strategic.expo.app") // Allow requests from
                                                                                               // your frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("*") // Allowed headers
                        .allowCredentials(true); // Allow credentials like cookies (if needed)
                // You might need a separate mapping for the WebSocket endpoint if it's not
                // under /api
                registry.addMapping("/ws/**")
                        .allowedOrigins(
                                "https://strategic.expo.app",
                                "http://localhost:8081",
                                "exp://192.168.178.130:8081"
                        )
                        .allowedMethods("GET", "POST", "OPTIONS", "PUT", "PATCH", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);

            }
        };
    }
}

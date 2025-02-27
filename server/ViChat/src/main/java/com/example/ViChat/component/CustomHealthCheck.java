package com.example.ViChat.component;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class CustomHealthCheck implements HealthIndicator {
    private final Random randomizer = new Random();
    private final List<Integer> statusCodes = List.of(200, 204, 401, 404, 503);
    @Override
    public Health health() {
        int randomStatusCode = statusCodes.get(randomizer.nextInt(statusCodes.size()));
        Health.Builder healthBuilder = new Health.Builder();
        return (switch(randomStatusCode) {
            case 200, 204 -> healthBuilder.up()
                    .withDetail("External_Service", "Service is Up and Running ✅")
                    .withDetail("url", "https://example.com");
            case 503 -> healthBuilder.down()
                    .withDetail("External_Service", "Service is Down 🔻")
                    .withDetail("alternative_url", "https://alt-example.com");
            default -> healthBuilder.unknown().withException(new RuntimeException("Received status: " + randomStatusCode));
        }).build();
    }
}

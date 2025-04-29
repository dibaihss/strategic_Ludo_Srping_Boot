package Services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    // Store IP and count per minute
    private final Map<String, Map<LocalDateTime, Integer>> requestCounts = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS_PER_MINUTE = 2; // Adjust as needed

    public boolean isRateLimited(String clientIp) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minuteKey = now.withSecond(0).withNano(0); // Round to the current minute
        
        Map<LocalDateTime, Integer> ipCounts = requestCounts.computeIfAbsent(clientIp, 
                k -> new ConcurrentHashMap<>());
        
        // Clean up old entries (older than 5 minutes)
        ipCounts.keySet().removeIf(minute -> minute.isBefore(minuteKey.minusMinutes(5)));
        
        // Get current count for this minute
        int currentCount = ipCounts.getOrDefault(minuteKey, 0);
        
        // Check if limit exceeded
        if (currentCount >= MAX_REQUESTS_PER_MINUTE) {
            return true; // Rate limited
        }
        
        // Increment counter
        ipCounts.put(minuteKey, currentCount + 1);
        return false; // Not rate limited
    }
}
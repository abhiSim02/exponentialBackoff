package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RetryConfig {

    @Value("${retry.max-retries:5}")
    private int maxRetries;

    @Value("${retry.initial-delay:1000}")
    private long initialDelay;

    @Value("${retry.max-delay:8000}")
    private long maxDelay;

    public int getMaxRetries() {
        return maxRetries;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getMaxDelay() {
        return maxDelay;
    }
}

package org.example.util;

import org.example.config.RetryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;

@Component
public class ConnectionRetry {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionRetry.class);

    private final RetryConfig retryConfig;

    public ConnectionRetry(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }
    public <T> String executeWithRetry(Supplier<T> task, Set<Class<? extends Exception>> retryableExceptions) {
        int maxRetries = retryConfig.getMaxRetries();
        long initialDelay = retryConfig.getInitialDelay();
        long maxDelay = retryConfig.getMaxDelay();

        int attempt = 0;
        long delay = initialDelay;

        while (true) {
            try {
                logAttempt(attempt);
                T result = task.get();
                logger.info("Task executed successfully on attempt {}.", attempt + 1);
                return "Success: " + result;
            } catch (Exception ex) {
                attempt++;
                if (shouldStopRetry(attempt, maxRetries, ex, retryableExceptions)) {
                    return "Failure: Max retries reached or non-retryable exception.";
                }
                delay = handleRetryDelay(delay, maxDelay);
            }
        }
    }

    private String logAttempt(int attempt) {
        String message = "Attempting task execution, attempt number: " + (attempt + 1);
        logger.info(message);
        return message;
    }

    private boolean shouldStopRetry(int attempt, int maxRetries, Exception ex, Set<Class<? extends Exception>> retryableExceptions) {
        if (attempt >= maxRetries) {
            logger.error("Max retries reached. Task failed after {} attempts.", maxRetries);
            return true;
        }
        if (retryableExceptions.stream().noneMatch(e -> e.isAssignableFrom(ex.getClass()))) {
            logger.error("Non-retryable exception encountered: {}", ex.getClass().getSimpleName(), ex);
            return true;
        }
        logger.warn("Attempt {} failed due to {}. Retrying...", attempt, ex.getClass().getSimpleName());
        return false;
    }

    private long handleRetryDelay(long delay, long maxDelay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Retry interrupted.", e);
            throw new RuntimeException("Retry interrupted", e);
        }
        long newDelay = Math.min(delay * 2, maxDelay);
        logger.info("Retrying after {}ms. Next delay set to {}ms.", delay, newDelay);
        return newDelay;
    }
}

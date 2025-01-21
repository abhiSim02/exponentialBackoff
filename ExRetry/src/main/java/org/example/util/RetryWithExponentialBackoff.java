package org.example.util;

import java.util.Set;
import java.util.function.Supplier;

public class RetryWithExponentialBackoff {

    public static <T> T executeWithRetry(Supplier<T> task, int maxRetries, long initialDelay, long maxDelay, Set<Class<? extends Exception>> retryableExceptions) {
        int attempt = 0;
        long delay = initialDelay;

        while (true) {
            try {
                System.out.println("Attempting task execution, attempt number: " + (attempt + 1));
                return task.get(); // Execute the task
            } catch (Exception ex) {
                attempt++;

                // Check if the exception is retryable
                if (retryableExceptions.stream().noneMatch(e -> e.isAssignableFrom(ex.getClass()))) {
                    System.out.println("Non-retryable exception encountered: " + ex.getClass().getSimpleName());
                    throw ex;
                }

                if (attempt >= maxRetries) {
                    System.out.println("Max retries reached. Task failed.");
                    throw new RuntimeException("Task failed after " + maxRetries + " retries", ex);
                }

                System.out.println("Attempt " + attempt + " failed. Retrying in " + delay + "ms...");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Retry interrupted.");
                    throw new RuntimeException("Retry interrupted", e);
                }

                // Increase the delay exponentially, but cap it
                delay = Math.min(delay * 2, maxDelay);
            }
        }
    }
}

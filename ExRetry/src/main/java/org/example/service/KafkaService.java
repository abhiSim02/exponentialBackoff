package org.example.service;

import org.example.util.RetryWithExponentialBackoff;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class KafkaService {

    public void connectToKafka() {
        RetryWithExponentialBackoff.executeWithRetry(() -> {
            System.out.println("Connecting to Kafka...");
            if (Math.random() < 0.7) { // Simulate Kafka connection failure
                throw new RuntimeException("Kafka Connection Failed!");
            }
            System.out.println("Connected to Kafka successfully!");
            return null;
        }, 5, 1000, 8000, Set.of(RuntimeException.class));
    }
}

package org.example.service;

import org.example.util.ConnectionRetry;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class KafkaService {

    private final ConnectionRetry connectionRetry;

    public KafkaService(ConnectionRetry connectionRetry) {
        this.connectionRetry = connectionRetry;
    }

    public String connectToKafka() {
        return connectionRetry.executeWithRetry(() -> {
            simulateKafkaConnection();
            return "Kafka Connection Successful";
        }, Set.of(RuntimeException.class));
    }

    private void simulateKafkaConnection() {
        if (Math.random() < 0.7) {
            throw new RuntimeException("Kafka Connection Failed!");
        }
    }
}

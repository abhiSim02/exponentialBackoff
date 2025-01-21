package org.example.service;

import org.example.util.RetryWithExponentialBackoff;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SftpService {

    private int sftpFailureCount = 0; // Counter to simulate failure

    public void connectToSftp() {
        RetryWithExponentialBackoff.executeWithRetry(() -> {
            System.out.println("Connecting to SFTP...");
            if (sftpFailureCount < 2) { // Fail the first 2 attempts
                sftpFailureCount++;
                throw new RuntimeException("SFTP Connection Failed!");
            }
            System.out.println("Connected to SFTP successfully!");
            return null;
        }, 5, 1000, 8000, Set.of(RuntimeException.class));
    }
}

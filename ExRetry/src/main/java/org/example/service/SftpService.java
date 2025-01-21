package org.example.service;

import org.example.util.ConnectionRetry;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SftpService {

    private final ConnectionRetry connectionRetry;

    public SftpService(ConnectionRetry connectionRetry) {
        this.connectionRetry = connectionRetry;
    }

    public String connectToSftp() {
        return connectionRetry.executeWithRetry(() -> {
            simulateSftpConnection();
            return "SFTP Connection Successful";
        }, Set.of(RuntimeException.class));
    }

    private void simulateSftpConnection() {
        if (Math.random() < 0.7) {
            throw new RuntimeException("SFTP Connection Failed!");
        }
    }
}

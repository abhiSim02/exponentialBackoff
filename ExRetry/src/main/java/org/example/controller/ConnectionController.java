package org.example.controller;

import org.example.service.KafkaService;
import org.example.service.SftpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

    private final SftpService sftpService;
    private final KafkaService kafkaService;

    public ConnectionController(SftpService sftpService, KafkaService kafkaService) {
        this.sftpService = sftpService;
        this.kafkaService = kafkaService;
    }

    @GetMapping("/retry-sftp")
    public String retrySftp() {
        return sftpService.connectToSftp();
    }

    @GetMapping("/retry-kafka")
    public String retryKafka() {
        return kafkaService.connectToKafka();
    }
}

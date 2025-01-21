package org.example.controller;

import org.example.service.KafkaService;
import org.example.service.SftpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    private final SftpService sftpService;
    private final KafkaService kafkaService;

    public ExampleController(SftpService sftpService, KafkaService kafkaService) {
        this.sftpService = sftpService;
        this.kafkaService = kafkaService;
    }

    @GetMapping("/retry-sftp")
    public String retrySftp() {
        sftpService.connectToSftp();
        return "SFTP connection task executed with retries!";
    }

    @GetMapping("/retry-kafka")
    public String retryKafka() {
        kafkaService.connectToKafka();
        return "Kafka connection task executed with retries!";
    }
}

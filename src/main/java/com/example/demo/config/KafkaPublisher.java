package com.example.demo.config;

import com.example.demo.models.Device;
import com.example.demo.models.DeviceInfo;
import com.example.demo.repos.DeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
public class KafkaPublisher {

    private final Random random = new Random();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private DeviceRepository deviceRepository;
    private List<String> deviceIds;

    @PostConstruct
    public void init() {
        this.deviceIds = deviceRepository.findAll().stream().map(Device::getId).toList();
    }

    //    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    public void pushToKafka() {
        log.info("creating device info");
        var di = DeviceInfo.builder()
                .deviceId(deviceIds.get(random.nextInt(this.deviceIds.size())))
                .values(Map.of(
                        "rpm", (random.nextLong(100, 10000)),
                        "voltage", (random.nextLong(100, 400)),
                        "compressorState", (random.nextBoolean()),
                        "temperature", (random.nextFloat(100))))
                .build();
        try {
            kafkaTemplate.send("device_info", new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(di));
        } catch (Exception e) {
            log.error("error {}", e.getMessage(), e);
        }
    }
}

package com.example.demo.config;

import com.example.demo.models.DeviceInfo;
import com.example.demo.services.DeviceInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @Autowired
    private DeviceInfoService deviceInfoService;

    //    @KafkaListener(topics = "device_info", groupId = "device_info")
    public void getDeviceInfo(String deviceInfoString) {
        log.info("got device info");
        try {
            deviceInfoService.uploadInfo(new ObjectMapper().registerModule(new JavaTimeModule()).readValue(deviceInfoString, DeviceInfo.class));
        } catch (Exception e) {
            log.error("error {}", e.getMessage(), e);
        }
    }
}

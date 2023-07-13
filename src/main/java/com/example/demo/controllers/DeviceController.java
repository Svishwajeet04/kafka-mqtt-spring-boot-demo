package com.example.demo.controllers;

import com.example.demo.config.KafkaPublisher;
import com.example.demo.models.Device;
import com.example.demo.services.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/devices")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private KafkaPublisher kafkaPublisher;

    @GetMapping({"/{id}", ""})
    public ResponseEntity<Object> getDevice(@PathVariable(required = false) String id) {
        return ResponseEntity.ok(deviceService.getDevice(id));
    }

    @PostMapping
    public ResponseEntity<Object> addDevice(@RequestBody Device device) {
        try {
            device.setCreationTime(LocalDateTime.now());
            var res = deviceService.addDevice(device);
            kafkaPublisher.init();
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            log.error("error : {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Object> deleteDevice(@PathVariable String id) {
        var res = deviceService.deleteDevice(id);
        kafkaPublisher.init();
        return res;
    }
}

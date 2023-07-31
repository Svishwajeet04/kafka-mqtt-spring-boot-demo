package com.example.demo.services;

import com.example.demo.config.MqttConfig;
import com.example.demo.models.DeviceInfo;
import com.example.demo.repos.DeviceInfoRepository;
import com.example.demo.repos.DeviceRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
public class DeviceInfoService {

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AlertService alertService;

    public ResponseEntity<Object> uploadInfo(@Valid DeviceInfo deviceInfo) {
        boolean exists = deviceRepository.existsById(deviceInfo.getDeviceId());
        if (exists) {
            deviceInfo.setCreationTime(LocalDateTime.now());
            var deviceInfoSaved = deviceInfoRepository.save(deviceInfo);
            alertService.checkForAlert(deviceInfoSaved);
            MqttConfig.publishMessageToTopic("device_info", deviceInfoSaved);
            return ResponseEntity.ok(deviceInfoSaved);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> retrieveDataByDevice(String deviceId, Long sinceTimestamp, String[] props) {
        try {
            var sinceTime = LocalDateTime.from(Instant.ofEpochMilli(sinceTimestamp).atZone(ZoneId.systemDefault()));
            Query query = Query.query(Criteria.where("creationTime").gte(sinceTime).and("deviceId").is(deviceId));
            for (String prop : props) {
                query.fields().project(ArithmeticOperators.Multiply.valueOf("values.".concat(prop)).multiplyBy(1000)).as("values.".concat(prop));
            }
            return ResponseEntity.ok(mongoTemplate.find(query, Document.class, "device_info"));
        } catch (Exception e) {
            log.error("error {}", e.getMessage(), e);
            throw e;
        }
    }
}

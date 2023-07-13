package com.example.demo.services;

import com.example.demo.config.MqttConfig;
import com.example.demo.models.*;
import com.example.demo.repos.AlertRepository;
import com.example.demo.repos.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private DeviceRepository deviceRepository;

    public void checkForAlert(DeviceInfo deviceInfo) {
        var violationCheckRes = checkViolations(deviceInfo);
        if (violationCheckRes != null && violationCheckRes.size() > 0) {
            log.warn("device is in critical state");
            boolean mailSent = false;
            var alert = alertRepository.save(Alert.builder()
                    .deviceInfo(deviceInfo)
                    .creationTime(LocalDateTime.now())
                    .alertViolationChecks(violationCheckRes)
                    .emailSent(true)
                    .build());
            try {
//                mailService.sendEmailForAlert(alert);
                MqttConfig.publishMessageToTopic("device_alert", alert);
            } catch (Exception e) {
                log.error("error sending mail {}", e.getMessage(), e);
            }
            log.warn("alert : {}", alert);
        }
    }

    public List<AlertViolationCheck> checkViolations(DeviceInfo deviceInfo) {
        Device device = deviceRepository.findById(deviceInfo.getDeviceId()).orElseThrow();
        List<AlertViolationCheck> alertViolationChecks = new ArrayList<>();
        var values = deviceInfo.getValues();
        if (device.getAlarms() != null) {
            for (Alarm alarm : device.getAlarms()) {
                try {
                    if (values.containsKey(alarm.getProperty()) &&
                            ((Number) values.get(alarm.getProperty())).doubleValue() > alarm.getValue().doubleValue())
                        alertViolationChecks.add(AlertViolationCheck.builder()
                                .alertViolationOf(alarm.getProperty().toUpperCase())
                                .requiredValue(alarm.getValue())
                                .actualValue(values.get(alarm.getProperty()))
                                .build());
                } catch (Exception e) {
                    log.error("error during alert violation check {}", e.getMessage(), e);
                }
            }
        }
        return alertViolationChecks;
    }

    public ResponseEntity<Object> getAlertByDeviceId(String deviceID, Long sinceTimestamp) {
        var sinceTime = LocalDateTime.from(Instant.ofEpochMilli(sinceTimestamp).atZone(ZoneId.systemDefault()));
        return ResponseEntity.ok(alertRepository.findByCreationTimeGreaterThanAndDeviceInfo_DeviceId(sinceTime, deviceID, Sort.by(Sort.Direction.DESC, "creationTime")));
    }
}

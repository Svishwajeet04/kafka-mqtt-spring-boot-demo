package com.example.demo.services;

import com.example.demo.models.Alarm;
import com.example.demo.models.Device;
import com.example.demo.repos.AlarmRepository;
import com.example.demo.repos.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AlarmService {
    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;


    public ResponseEntity<Object> addAlarm(Alarm alarm) {
        return ResponseEntity.ok(alarmRepository.save(alarm));
    }

    public ResponseEntity<Object> deleteAlarm(String id) {
        List<Device> deviceList = deviceRepository.findByAlarmId(id);
        if (deviceList != null && !deviceList.isEmpty()) {
            deviceList.forEach(device -> {
                device.setAlarms(device.getAlarms().stream().filter(a -> !Objects.equals(a.getId(), id)).toList());
                deviceRepository.save(device);
            });
        }
        alarmRepository.deleteById(id);
        return ResponseEntity.ok("alarm successfully deleted");
    }

    public ResponseEntity<Object> getAlarms() {
        return ResponseEntity.ok(alarmRepository.findAll());
    }

    public ResponseEntity<Object> findById(String id) {
        var res = alarmRepository.findById(id);
        return res.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> toggleAlarmToDevice(String deviceId, String alarmId, Boolean add) {
        return deviceRepository.findById(deviceId)
                .map(device -> {
                    var alarms = device.getAlarms() != null && device.getAlarms().size() > 0 ?
                            new java.util.HashSet<>((device.getAlarms()))
                            : new HashSet<Alarm>();
                    var alarmOp = alarmRepository.findById(alarmId);
                    if (alarmOp.isPresent()) {
                        boolean b = add ? alarms.add(alarmOp.get()) : alarms.remove(alarmOp.get());
                        log.info("alarm {} {} device {} {}", alarmId, add ? "added to" : "removed from", deviceId, b ? "successfully" : "failed");
                    } else {
                        return ResponseEntity.badRequest().body("alarm not found");
                    }
                    device.setAlarms(alarms.stream().toList());
                    return ResponseEntity.ok(deviceRepository.save(device));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("device not found"));
    }
}

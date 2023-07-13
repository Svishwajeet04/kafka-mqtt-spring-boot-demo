package com.example.demo.controllers;

import com.example.demo.models.Alarm;
import com.example.demo.services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @PostMapping
    public ResponseEntity<Object> addAlarm(@RequestBody Alarm alarm) {
        return alarmService.addAlarm(alarm);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Object> deleteAlarm(@PathVariable String id) {
        return alarmService.deleteAlarm(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAlarms() {
        return alarmService.getAlarms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAlarmById(@PathVariable String id) {
        return alarmService.findById(id);
    }

    @GetMapping("{alarmId}/devices/{deviceId}/add")
    public ResponseEntity<?> addAlarmToDevice(@PathVariable String deviceId, @PathVariable String alarmId) {
        return alarmService.toggleAlarmToDevice(deviceId, alarmId, true);
    }

    @GetMapping("{alarmId}/devices/{deviceId}/remove")
    public ResponseEntity<?> removeAlarmFromDevice(@PathVariable String deviceId, @PathVariable String alarmId) {
        return alarmService.toggleAlarmToDevice(deviceId, alarmId, false);
    }

}

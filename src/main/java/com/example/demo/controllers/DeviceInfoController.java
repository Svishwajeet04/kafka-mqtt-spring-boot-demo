package com.example.demo.controllers;

import com.example.demo.models.DeviceInfo;
import com.example.demo.services.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices/info")
public class DeviceInfoController {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @PostMapping
    public ResponseEntity<Object> uploadInfo(@RequestBody DeviceInfo deviceInfo) {
        return deviceInfoService.uploadInfo(deviceInfo);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Object> retrieveDataByDevice(@PathVariable String deviceId, @RequestParam Long sinceTimestamp, @RequestParam String[] props) {
        return deviceInfoService.retrieveDataByDevice(deviceId, sinceTimestamp, props);
    }
}

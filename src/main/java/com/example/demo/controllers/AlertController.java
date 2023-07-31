package com.example.demo.controllers;

import com.example.demo.services.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/device/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAlertByDeviceId(@PathVariable String id, @RequestParam Long sinceTimestamp) {
        return alertService.getAlertByDeviceId(id, sinceTimestamp);
    }
}

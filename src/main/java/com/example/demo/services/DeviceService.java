package com.example.demo.services;

import com.example.demo.models.Device;
import com.example.demo.repos.DeviceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public Object getDevice(String id) {
        if (id == null) {
            return deviceRepository.findAll();
        } else {
            return deviceRepository.findById(id);
        }
    }

    public Device addDevice(@Valid Device device) throws Exception {
        return deviceRepository.save(device);
    }

    public ResponseEntity<Object> deleteDevice(String id) {
        var res = ResponseEntity.ok((Object) deviceRepository.findById(id));
        deviceRepository.deleteById(id);
        return res;
    }
}

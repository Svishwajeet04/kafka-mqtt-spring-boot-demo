package com.example.demo.repos;

import com.example.demo.models.Alert;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByCreationTimeGreaterThanAndDeviceInfo_DeviceId(LocalDateTime sinceTime, String deviceID, Sort creationTime);
}

package com.example.demo.repos;

import com.example.demo.models.DeviceInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceInfoRepository extends MongoRepository<DeviceInfo, String> {
}

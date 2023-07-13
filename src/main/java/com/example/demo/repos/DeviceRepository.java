package com.example.demo.repos;

import com.example.demo.models.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

    @Query("""
            {
            alarms : $id
            }
            """)
    List<Device> findByAlarmId(String id);
}

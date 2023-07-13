package com.example.demo.repos;

import com.example.demo.models.Alarm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends MongoRepository<Alarm, String> {
}

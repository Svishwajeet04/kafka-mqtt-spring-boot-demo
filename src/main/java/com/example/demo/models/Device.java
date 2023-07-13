package com.example.demo.models;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "devices")
@Data
public class Device {

    List<Alarm> alarms;
    @Id
    @Null
    private String id;
    private String name;
    private LocalDateTime creationTime;
}

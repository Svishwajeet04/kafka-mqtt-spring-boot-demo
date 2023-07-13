package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "alert")
public class Alert {
    @Id
    private String id;

    private DeviceInfo deviceInfo;

    private LocalDateTime creationTime;

    private List<AlertViolationCheck> alertViolationChecks;

    private boolean emailSent;
}

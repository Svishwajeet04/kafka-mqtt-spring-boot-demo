package com.example.demo.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "device_info")
@Data
public class DeviceInfo {

    Map<String, Object> values;
    @Id
    @Null
    private String id;
    @Null
    private LocalDateTime creationTime;
    @NotNull
    private String deviceId;

}

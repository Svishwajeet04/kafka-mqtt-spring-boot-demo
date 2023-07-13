package com.example.demo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptionsBuilder;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.util.UUID;

@Slf4j
public class MqttConfig {

    private static final String MQTT_SERVER_ADDRESS = "tcp://127.0.0.1:1883";
    private static final String MQTT_PUBLISHER_ID = UUID.randomUUID().toString();
    private static MqttClient instance;

    private MqttConfig() {
    }

    private static MqttClient getInstance() {
        try {
            if (instance == null) {
                instance = new MqttClient(MQTT_SERVER_ADDRESS, MQTT_PUBLISHER_ID);
            }
            if (!instance.isConnected()) {
                MqttConnectionOptions options = new MqttConnectionOptionsBuilder()
                        .automaticReconnect(true)
                        .connectionTimeout(10)
                        .cleanStart(true)
                        .username("root")
                        .password("1234".getBytes())
                        .build();
                instance.connect(options);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static void publishMessageToTopic(String topic, Object payload) {
        MqttMessage message = new MqttMessage();
        try {
            message.setPayload(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(payload));
            message.setQos(1);
            message.setRetained(true);
            getInstance().publish(topic, message);
            log.info("message sent to MQTT broker {}", payload);
        } catch (JsonProcessingException | MqttException e) {
            log.error("error sending message to MQTT broker {}", payload, e);
        }
    }

}

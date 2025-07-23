package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import com.gitee.swsk33.swmmsensorthings.eventbus.property.SensorThingsServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 测试用的MQTT客户端
 */
@Slf4j
@Configuration
public class TestMqttClientConfig {

	@Autowired
	private SensorThingsServerProperties properties;

	@Bean("testMqttClient")
	public MqttClient mqttClient() throws Exception {
		// 连接SensorThings服务器MQTT端口
		MqttClient subscriberClient = new MqttClient(String.format("tcp://%s:%d", properties.getMqttBrokerHost(), properties.getMqttBrokerPort()), "SensorThings-Publisher", new MemoryPersistence());
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connectOptions.setAutomaticReconnect(true);
		// 连接MQTT Broker
		subscriberClient.connect(connectOptions);
		log.info("已完成测试用MQTT客户端配置！");
		return subscriberClient;
	}

}
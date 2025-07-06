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
 * 自动完成MQTT配置，并订阅全部传感器数据流
 */
@Slf4j
@Configuration
public class MqttClientConfig {

	@Autowired
	private SensorThingsServerProperties properties;

	@Bean
	public MqttClient mqttClient() throws Exception {
		// 连接SensorThings服务器MQTT端口
		MqttClient subscriberClient = new MqttClient(String.format("tcp://%s:%d", properties.getMqttBrokerHost(), properties.getMqttBrokerPort()), "SensorThings-Subscriber", new MemoryPersistence());
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connectOptions.setAutomaticReconnect(true);
		// 连接MQTT Broker
		subscriberClient.connect(connectOptions);
		log.info("------- 传感数据驱动订阅，启动！ -------");
		return subscriberClient;
	}

}
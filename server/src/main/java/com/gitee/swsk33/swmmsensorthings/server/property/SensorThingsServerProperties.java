package com.gitee.swsk33.swmmsensorthings.server.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于SensorThings服务端的配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.sensor-things-server")
public class SensorThingsServerProperties {

	/**
	 * SensorThings服务器HTTP地址
	 */
	private String host;

	/**
	 * SensorThings服务器HTTP端口
	 */
	private int port;

	/**
	 * SensorThings的MQTT Broker地址
	 */
	private String mqttBrokerHost;

	/**
	 * SensorThings的MQTT Broker端口
	 */
	private int mqttBrokerPort;

}
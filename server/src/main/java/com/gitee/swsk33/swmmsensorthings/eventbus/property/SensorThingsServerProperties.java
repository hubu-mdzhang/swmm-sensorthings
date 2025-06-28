package com.gitee.swsk33.swmmsensorthings.eventbus.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于SensorThings服务端的配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.sensor-things-eventbus.sensor-things-server")
public class SensorThingsServerProperties {

	/**
	 * SensorThings服务器HTTP地址<br>
	 * 例如：<code>http://localhost:8080/my-sensorthings-server</code><br>
	 * 不要以<code>/</code>结尾，无需带上版本号部分（例如：<code>/v1.1</code>）
	 */
	private String url;

	/**
	 * SensorThings的MQTT Broker地址
	 */
	private String mqttBrokerHost;

	/**
	 * SensorThings的MQTT Broker端口
	 */
	private int mqttBrokerPort;

}
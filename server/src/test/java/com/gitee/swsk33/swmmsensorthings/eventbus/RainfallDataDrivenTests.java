package com.gitee.swsk33.swmmsensorthings.eventbus;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.eventbus.param.SensorThingsExpandProperty;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.SensorThingsServerProperties;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@SpringBootTest
public class RainfallDataDrivenTests {

	@Autowired
	private SensorThingsServerProperties properties;

	@Autowired
	private SensorThingsObjectClient client;

	@Test
	@DisplayName("发布降水序列数据")
	void publishRainfall() throws Exception {
		// 查询对应雨量计
		String name = "RainGage-rainfall";
		// 查询数据流
		Datastream datastream = client.getByName(name, Datastream.class, SensorThingsExpandProperty.getExpandProperty(Datastream.class));
		if (datastream == null) {
			log.error("数据流不存在！");
			return;
		}
		log.info("已查询到对应数据流id：{}，名称：{}", datastream.getId(), datastream.getName());
		// 创建MQTT客户端对象
		MqttClient publisherClient = new MqttClient(String.format("tcp://%s:%d", properties.getMqttBrokerHost(), properties.getMqttBrokerPort()), "Rainfall-Publisher", new MemoryPersistence());
		// 配置客户端
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connectOptions.setAutomaticReconnect(true);
		publisherClient.connect(connectOptions);
		// 读取数据
		String filepath = Paths.get("input-data", "rainfall.json").toAbsolutePath().toString();
		byte[] data = FileUtil.readBytes(filepath);
		List<Observation> rainfallData = JSON.parseArray(data, Observation.class);
		// 客户端发布
		for (Observation observation : rainfallData) {
			MqttMessage message = new MqttMessage();
			message.setPayload(JSON.toJSONBytes(observation));
			publisherClient.publish(String.format("v1.1/Datastreams(%d)/Observations", (int) datastream.getId()), message);
			Thread.sleep(350);
		}
		log.info("全部发布完毕！");
		Thread.sleep(6000);
	}

}
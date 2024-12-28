package com.gitee.swsk33.swmmsensorthings.eventbus.config;

import com.gitee.swsk33.swmmsensorthings.eventbus.client.SensorThingsObjectClient;
import com.gitee.swsk33.swmmsensorthings.eventbus.param.SensorThingsExpandProperty;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.SensorThingsServerProperties;
import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt.RainGageSubscriber;
import com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.github.swsk33.swmmjava.param.ObjectTypeCode.GAGE;

/**
 * 自动完成MQTT配置，并订阅全部传感器数据流
 */
@Slf4j
@Configuration
@DependsOn({"SWMMConfig", "sensorThingsObjectInitialize"})
public class MqttClientConfig {

	@Autowired
	private SWMM swmm;

	@Autowired
	private SensorThingsObjectClient client;

	@Autowired
	private SensorThingsServerProperties properties;

	@Autowired
	private BeanFactory beanFactory;

	@Bean
	public MqttClient mqttClient() throws Exception {
		// 获取全部雨量计
		List<VisualObject> gages = swmm.getObjectList(GAGE);
		// 查询对应传感器观测属性的数据流
		List<Datastream> datastreams = new ArrayList<>();
		for (VisualObject gage : gages) {
			Set<String> properties = PropertyReadUtils.getComputedPropertyNames(gage);
			// 查找每个属性对应的数据流
			for (String property : properties) {
				Datastream datastream = client.getByName(NameUtils.generateObservedPropertyName(gage, property), Datastream.class, SensorThingsExpandProperty.getExpandProperty(Datastream.class));
				if (datastream != null) {
					datastreams.add(datastream);
				}
			}
		}
		// 订阅对应数据流
		MqttClient subscriberClient = new MqttClient(String.format("tcp://%s:%d", properties.getMqttBrokerHost(), properties.getMqttBrokerPort()), "SensorThings-Subscriber", new MemoryPersistence());
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connectOptions.setAutomaticReconnect(true);
		// 连接MQTT Broker
		subscriberClient.connect(connectOptions);
		// 订阅全部数据流
		for (Datastream datastream : datastreams) {
			subscriberClient.subscribe(String.format("v1.1/Datastreams(%d)/Observations?$expand=%s", (int) datastream.getId(), String.join(",", SensorThingsExpandProperty.getExpandProperty(Observation.class))), beanFactory.getBean(RainGageSubscriber.class));
		}
		log.info("------- 传感数据驱动订阅，启动！ -------");
		return subscriberClient;
	}

}
package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.HydrologicalJobService;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils.getObservationSensorName;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RainGageSubscriber implements IMqttMessageListener {

	@Autowired
	private HydrologicalJobService HydrologicalJobService;

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		// 解析消息
		Observation observation = JSON.parseObject(message.getPayload(), Observation.class);
		// 解析传感器与属性名
		String sensor = getObservationSensorName(observation.getDatastream().getName());
		// 输入水文模型并运行一次
		try {
//			Result<Void> result = HydrologicalProcessService.stepRun(sensor, DoubleValueUtils.parseDouble(observation.getResult()));
//			if (!result.isSuccess()) {
//				log.error("输入并模拟失败！传感器：{}，数值：{}", sensor, observation.getResult());
//				return;
//			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("已输入{}的降水量数据，数值：{}，时间：{}，模型运行一个步长...", sensor, observation.getResult(), observation.getPhenomenonTime());
	}

}
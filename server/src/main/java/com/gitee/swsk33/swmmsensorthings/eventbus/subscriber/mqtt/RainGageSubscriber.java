package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.util.DoubleValueUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils.getObservationSensorName;
import static io.github.swsk33.swmmjava.param.ObjectTypeCode.GAGE;

/**
 * 用于订阅观测数据、并输入到SWMM模型的MQTT订阅者，一个模型实例对应一个订阅者
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RainGageSubscriber implements IMqttMessageListener {

	/**
	 * 水文模型对象
	 */
	@Setter
	private SWMM swmm;

	/**
	 * 构造函数
	 *
	 * @param swmm 输入SWMM对象
	 */
	public RainGageSubscriber(SWMM swmm) {
		this.swmm = swmm;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		// 解析消息
		Observation observation = JSON.parseObject(message.getPayload(), Observation.class);
		// 解析传感器与属性名
		String sensor = getObservationSensorName(observation.getDatastream().getName());
		// 输入水文模型并运行一次
		try {
			// 获取雨量计对象
			VisualObject gage = swmm.getObject(GAGE, sensor);
			if (gage == null) {
				log.error("找不到传感器：{}", sensor);
				return;
			}
			// 输入数据
			RainGage patch = new RainGage();
			patch.setIndex(gage.getIndex());
			patch.setRainfall(DoubleValueUtils.parseDouble(observation.getResult()));
			swmm.setValue(patch);
			// 执行模型一个步长
			double elapsed = swmm.step();
			if (elapsed == 0) {
				log.warn("模拟已结束！");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("已输入{}的降水量数据，数值：{}，时间：{}，模型运行一个步长...", sensor, observation.getResult(), observation.getPhenomenonTime());
	}

}
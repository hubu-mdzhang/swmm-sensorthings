package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.ObservationPool;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import io.github.swsk33.swmmjava.SWMM;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 用于订阅观测数据、并输入到SWMM模型的MQTT订阅者，一个模型实例的一个传感器对应一个订阅者
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RainGageSubscriber implements IMqttMessageListener {

	/**
	 * 当前订阅者对应的数据缓存池
	 */
	private final ObservationPool dataPool;

	/**
	 * 构造函数
	 *
	 * @param swmm 输入SWMM对象
	 */
	public RainGageSubscriber(SWMM swmm) {
		// 初始化数据缓存池
		this.dataPool = new ObservationPool(swmm);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		// 解析消息
		Observation data = JSON.parseObject(message.getPayload(), Observation.class);
		// 传递到数据缓存池
		this.dataPool.addData(data);
		// 调用缓存池，在时间步长数据集齐时驱动模型运行
		this.dataPool.runModel();
	}

}
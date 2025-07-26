package com.gitee.swsk33.swmmsensorthings.eventbus.stats;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.context.TransferDelayList;
import com.gitee.swsk33.swmmsensorthings.eventbus.helper.RequestClientHelper;
import com.gitee.swsk33.swmmsensorthings.eventbus.service.HydrologicalJobService;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * 用于比较HTTP和MQTT传输方式延迟的统计测试类型
 */
@Slf4j
@SpringBootTest
public class TransferDelayStatsTests implements InitializingBean {

	@Autowired
	private HydrologicalJobService hydrologicalJobService;

	@Resource(name = "testMqttClient")
	private MqttClient mqttClient;

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private TransferDelayList transferDelayList;

	private RequestClientHelper clientHelper;

	/**
	 * 初始化工作
	 */
	@Override
	public void afterPropertiesSet() {
		this.clientHelper = beanFactory.getBean(RequestClientHelper.class, "http://127.0.0.1:5354/api/http-receiver");
	}

	/**
	 * 使用HTTP协议发送数据
	 *
	 * @param taskId 任务id
	 * @param data   发送的数据
	 */
	private void sendHttpData(String taskId, Observation data) throws Exception {
		byte[] body = JSON.toJSONBytes(data);
		// 记录时间
		transferDelayList.httpSendTimeList.add(LocalDateTime.now());
		// 发请求
		clientHelper.post(String.format("/input/%s", taskId), body);
	}

	/**
	 * 使用MQTT协议发布数据
	 *
	 * @param datastreamId 数据流id
	 * @param data         发送的数据
	 */
	private void publishMqttData(int datastreamId, Observation data) throws Exception {
		MqttMessage message = new MqttMessage();
		message.setPayload(JSON.toJSONBytes(data));
		// 记录时间
		transferDelayList.mqttSendTimeList.add(LocalDateTime.now());
		mqttClient.publish(String.format("v1.1/Datastreams(%d)/Observations", datastreamId), message);
	}

}
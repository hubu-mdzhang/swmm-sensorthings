package com.gitee.swsk33.swmmsensorthings.eventbus.stats;

import com.alibaba.fastjson2.JSON;
import com.gitee.swsk33.swmmsensorthings.eventbus.context.TransferDelayList;
import com.gitee.swsk33.swmmsensorthings.eventbus.helper.RequestClientHelper;
import com.gitee.swsk33.swmmsensorthings.eventbus.task.SimulationTask;
import com.gitee.swsk33.swmmsensorthings.eventbus.util.DataUtils;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 用于比较HTTP和MQTT传输方式延迟的统计测试类型
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferDelayStatsTests implements InitializingBean {

	@Resource(name = "testMqttClient")
	private MqttClient mqttClient;

	@Autowired
	private BeanFactory beanFactory;

	@Resource(name = "processTaskPool")
	private ExecutorService taskPool;

	@Autowired
	private TransferDelayList transferDelayList;

	@LocalServerPort
	private int port;

	@Autowired
	private DataUtils dataUtils;

	private RequestClientHelper clientHelper;

	/**
	 * 初始化工作
	 */
	@Override
	public void afterPropertiesSet() {
		this.clientHelper = beanFactory.getBean(RequestClientHelper.class, String.format("http://127.0.0.1:%d/api/http-receiver", port));
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
		clientHelper.post(String.format("/input/%s", taskId), body).close();
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

	@Test
	@DisplayName("测试传输方法延迟")
	void testTransferDelay() throws Exception {
		// 创建水文模型任务
		SimulationTask httpTask = beanFactory.getBean(SimulationTask.class, "test-data/input.inp");
		taskPool.execute(httpTask);
		httpTask.waitForInitialization();
		// 获取id
		String id = httpTask.getId();
		log.info("已创建HTTP测试水文模型任务：{}", id);
		// 生成数据
		LocalDateTime start = LocalDateTime.of(2025, 7, 1, 12, 0, 0);
		List<Observation> dataList = dataUtils.generateObservations(1, start, 60, 10);
		for (Observation data : dataList) {
			sendHttpData(id, data);
		}
		log.info("HTTP测试完成！");
		List<Long> delay = new ArrayList<>();
		for (int i = 0; i < transferDelayList.httpSendTimeList.size(); i++) {
			LocalDateTime send = transferDelayList.httpSendTimeList.get(i);
			LocalDateTime receive = transferDelayList.httpReceiveTimeList.get(i);
			delay.add(Duration.between(send, receive).toNanos());
		}
		System.out.println(delay);
	}

}
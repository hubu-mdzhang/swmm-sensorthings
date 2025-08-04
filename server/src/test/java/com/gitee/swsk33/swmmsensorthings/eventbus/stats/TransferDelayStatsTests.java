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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
		// 记录时间
		transferDelayList.mqttSendTimeList.add(LocalDateTime.now());
		mqttClient.publish(String.format("v1.1/Datastreams(%d)/Observations", datastreamId), JSON.toJSONBytes(data), 0, false);
		Thread.sleep(150);
	}

	/**
	 * 该方法仅是一轮测试对比
	 * 测试步骤：
	 * 1. 生成三组数据，分别是100、500和1000个观测数据
	 * 2. 每组数据进行一轮测试，一轮测试过程中创建两个一样的水文模型模拟任务，分别测试HTTP和MQTT延迟
	 * 3. 记录每轮数据中每个步长延迟时间，单位ns
	 */
	@Test
	@DisplayName("测试传输方法延迟")
	void testTransferDelay() throws Exception {
		// 1. 创建数据
		LocalDateTime start = LocalDateTime.of(2025, 7, 1, 12, 0, 0);
		int stepLengthSecond = 60;
		int count = 100;
		List<Observation> dataList = dataUtils.generateObservations(1, start, stepLengthSecond, count);
		// 2. 执行一轮测试
		String inputFile = "test-data/input.inp";
		// 创建水文模型HTTP任务
		SimulationTask httpTask = beanFactory.getBean(SimulationTask.class, inputFile);
		Future<?> httpFuture = taskPool.submit(httpTask);
		httpTask.waitForInitialization();
		// 获取id
		String id = httpTask.getId();
		log.info("已创建HTTP测试水文模型任务：{}", id);
		for (Observation data : dataList) {
			sendHttpData(id, data);
		}
		log.info("HTTP测试完成！");
		httpTask.dispose();
		httpFuture.cancel(true);
		List<Long> httpDelayList = dataUtils.computeDelay(transferDelayList.httpSendTimeList, transferDelayList.httpReceiveTimeList);
		// 创建水文模型MQTT任务
		SimulationTask mqttTask = beanFactory.getBean(SimulationTask.class, inputFile);
		Future<?> mqttFuture = taskPool.submit(mqttTask);
		mqttTask.waitForInitialization();
		// 获取id
		id = mqttTask.getId();
		log.info("已创建MQTT测试水文模型任务：{}", id);
		for (Observation data : dataList) {
			publishMqttData(1, data);
		}
		log.info("MQTT测试完成！");
		mqttTask.dispose();
		mqttFuture.cancel(true);
		List<Long> mqttDelayList = dataUtils.computeDelay(transferDelayList.mqttSendTimeList, transferDelayList.mqttReceiveTimeList);
		// 保存输出结果
		Map<String, List<Long>> result = new HashMap<>();
		result.put("HTTP", httpDelayList);
		result.put("MQTT", mqttDelayList);
		dataUtils.writeCSV(String.format("test-output/transfer-delay-%d.csv", count), result);
	}

}
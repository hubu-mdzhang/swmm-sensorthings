package com.gitee.swsk33.swmmsensorthings.eventbus.subscriber;

import com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.http.HttpObservationReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据接收器管理上下文
 */
@Slf4j
@Component
public class DataReceiverContext {

	/**
	 * 存放全部SWMM模拟任务实例对应的HTTP数据接收器
	 * <ul>
	 *     <li>键：任务id</li>
	 *     <li>值：HTTP接收器</li>
	 * </ul>
	 */
	private static final Map<String, HttpObservationReceiver> HTTP_RECEIVER_MAP = new ConcurrentHashMap<>();

	/**
	 * 存放全部SWMM模拟任务实例对应订阅的传感器MQTT主题列表
	 * <ul>
	 *     <li>键：任务id</li>
	 *     <li>值：Topic列表</li>
	 * </ul>
	 */
	private static final Map<String, List<String>> MQTT_TOPIC_MAP = new ConcurrentHashMap<>();

	/**
	 * 添加HTTP接收器
	 *
	 * @param id       任务id
	 * @param receiver 对应的HTTP接收器
	 */
	public void addHttpReceiver(String id, HttpObservationReceiver receiver) {
		HTTP_RECEIVER_MAP.put(id, receiver);
	}

	/**
	 * 添加MQTT主题列表
	 *
	 * @param id     任务id
	 * @param topics 对应订阅的主题列表
	 */
	public void addMqttTopics(String id, List<String> topics) {
		MQTT_TOPIC_MAP.put(id, topics);
	}

	/**
	 * 获取HTTP接收器
	 *
	 * @param id 任务id
	 * @return 对应的HTTP接收器
	 */
	public HttpObservationReceiver getHttpReceiver(String id) {
		return HTTP_RECEIVER_MAP.get(id);
	}

	/**
	 * 获取MQTT主题列表
	 *
	 * @param id 任务id
	 * @return 对应的MQTT主题列表
	 */
	public List<String> getMqttTopics(String id) {
		return MQTT_TOPIC_MAP.get(id);
	}

	/**
	 * 移除HTTP接收器
	 *
	 * @param id 任务id
	 */
	public void removeHttpReceiver(String id) {
		HTTP_RECEIVER_MAP.remove(id);
	}

	/**
	 * 移除MQTT Topic列表
	 *
	 * @param id 任务id
	 */
	public void removeMqttTopics(String id) {
		MQTT_TOPIC_MAP.remove(id);
	}

}
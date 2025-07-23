package com.gitee.swsk33.swmmsensorthings.eventbus.context;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录延迟指标的列表
 */
@Component
public class DelayList {

	/**
	 * 记录每一个模拟步长执行时，使用HTTP发送观测数据的时间
	 */
	public final List<LocalDateTime> httpSendTimeList = new ArrayList<>();

	/**
	 * 记录每一个模拟步长执行时，使用HTTP接收到观测数据的时间
	 */
	public final List<LocalDateTime> httpReceiveTimeList = new ArrayList<>();

	/**
	 * 记录每一个模拟步长执行时，使用MQTT发布观测数据的时间
	 */
	public final List<LocalDateTime> mqttSendTimeList = new ArrayList<>();

	/**
	 * 记录每一个模拟步长执行时，使用MQTT订阅到观测数据的时间
	 */
	public final List<LocalDateTime> mqttReceiveTimeList = new ArrayList<>();

}
package com.gitee.swsk33.swmmsensorthings.eventbus.aop;

import com.gitee.swsk33.swmmsensorthings.eventbus.context.TransferDelayList;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 统计不同网络协议传输接收到数据时间的切面
 */
@Slf4j
@Aspect
@Component
public class TransferDelayStatsAspect {

	@Autowired
	private TransferDelayList transferDelayList;

	/**
	 * HTTP接收方法切面
	 */
	@Pointcut("execution(* com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.http.HttpObservationReceiver.receiveData(..))")
	public void httpReceive() {
	}

	/**
	 * MQTT订阅方法切面
	 */
	@Pointcut("execution(* com.gitee.swsk33.swmmsensorthings.eventbus.subscriber.mqtt.MqttObservationSubscriber.messageArrived(..))")
	public void mqttSubscribe() {
	}

	/**
	 * 记录接收到HTTP请求后的时间
	 */
	@After("httpReceive()")
	public void httpReceiveTime() {
		LocalDateTime now = LocalDateTime.now();
		log.info("接收到HTTP数据时间：{}", now);
		transferDelayList.httpReceiveTimeList.add(now);
	}

	/**
	 * 记录接收到MQTT数据后的时间
	 */
	@After("mqttSubscribe()")
	public void mqttSubscribeTime() {
		LocalDateTime now = LocalDateTime.now();
		log.info("接收到MQTT数据时间：{}", now);
		transferDelayList.mqttReceiveTimeList.add(now);
	}

}
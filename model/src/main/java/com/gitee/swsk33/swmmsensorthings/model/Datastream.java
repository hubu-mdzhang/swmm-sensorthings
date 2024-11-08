package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 数据流
 */
@Data
@ToString(callSuper = true)
public class Datastream extends SensorThingsObject {

	/**
	 * 观测类型
	 */
	private String observationType;

	/**
	 * 数据单位
	 */
	private JSONObject unitOfMeasurement;

	/**
	 * 观测区域
	 */
	@SensorThingsOptional
	private JSONObject observedArea;

	/**
	 * 观测现象时间
	 */
	@SensorThingsOptional
	private LocalDateTime phenomenonTime;

	/**
	 * 结果时间
	 */
	@SensorThingsOptional
	private LocalDateTime resultTime;

	/**
	 * 其它属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

	/**
	 * 对应物体
	 */
	private Thing thing;

	/**
	 * 对应传感器
	 */
	private Sensor sensor;

	/**
	 * 对应观测属性
	 */
	private ObservedProperty observedProperty;

}
package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 观测值
 */
@Data
public class Observation extends SensorThingsObject {

	/**
	 * 观测现象时间
	 */
	private LocalDateTime phenomenonTime;

	/**
	 * 观测结果时间
	 */
	private LocalDateTime resultTime;

	/**
	 * 观测结果值
	 */
	private Object result;

	/**
	 * 观测质量描述
	 */
	@SensorThingsOptional
	private Object[] resultQuality;

	/**
	 * 有效时间
	 */
	@SensorThingsOptional
	private LocalDateTime validTime;

	/**
	 * 观测参数
	 */
	@SensorThingsOptional
	private JSONObject parameters;

	/**
	 * 位于的数据流
	 */
	private Datastream datastream;

	/**
	 * 对应的兴趣要素
	 */
	private FeatureOfInterest featureOfInterest;

}
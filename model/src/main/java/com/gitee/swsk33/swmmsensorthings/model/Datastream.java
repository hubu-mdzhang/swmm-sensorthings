package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 数据流
 */
// TODO 模型映射可能存在问题：当Datastream表示降水数据时，那么Thing是什么？
// TODO 当Datastream为子区域、实体计算属性时，那么Sensor是什么？
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

	/**
	 * 创建一个Datastream对象，基于已有数据
	 *
	 * @param observation      数据流对应的观测结果，会将其datastream属性关联到该方法创建的数据流上
	 * @param observedProperty 数据流对应的观测属性
	 * @return 数据流对象
	 */
	public static Datastream create(Observation observation, ObservedProperty observedProperty) {
		Datastream datastream = new Datastream();
		// TODO 还需关联具体传感器，可考虑根据ObservedProperty生成
		// TODO 还需关联具体实体
		datastream.setName(observation.getName() + "datastream");
		datastream.setDescription("The datastream of observation " + observation.getName());
		datastream.setObservationType("");
		// TODO 根据水文系统设定判断单位
		datastream.setUnitOfMeasurement(new JSONObject());
		datastream.setObservedProperty(observedProperty);
		observation.setDatastream(datastream);
		return datastream;
	}

	/**
	 * 创建一个Datastream对象，基于已有数据和传感器信息
	 *
	 * @param observation      数据流对应的观测结果，会将其datastream属性关联到该方法创建的数据流上
	 * @param observedProperty 数据流对应的观测属性
	 * @param sensor           传感器信息
	 * @return 数据流对象
	 */
	public static Datastream create(Observation observation, ObservedProperty observedProperty, Sensor sensor) {
		Datastream datastream = create(observation, observedProperty);
		datastream.setSensor(sensor);
		return datastream;
	}

}
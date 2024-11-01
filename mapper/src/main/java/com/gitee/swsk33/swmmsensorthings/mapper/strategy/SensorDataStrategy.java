package com.gitee.swsk33.swmmsensorthings.mapper.strategy;

import com.gitee.swsk33.swmmsensorthings.model.Observation;
import com.gitee.swsk33.swmmsensorthings.model.ObservedProperty;
import io.github.swsk33.swmmjava.model.VisualObject;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 将不同SWMM可视对象数据转换成SensorThings数据的抽象策略类型
 */
public interface SensorDataStrategy {

	/**
	 * 从水文模型可视对象提取转换观测数据对象的方法
	 *
	 * @param object 原始可视对象
	 * @param time   数据对应的时间
	 * @return 转换后的观测数据列表
	 */
	List<Observation> mapObservation(VisualObject object, LocalDateTime time);

	/**
	 * 从水文模型可视对象提取转换全部观测属性对象的方法
	 *
	 * @param object 原始可视对象
	 * @return 转换后的观测属性列表
	 */
	List<ObservedProperty> mapObservedProperty(VisualObject object);

}
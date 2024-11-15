package com.gitee.swsk33.swmmsensorthings.mapper.util;

import io.github.swsk33.swmmjava.model.VisualObject;

/**
 * 用于对SensorThings对象统一命名的实用类
 */
public class NameUtils {

	/**
	 * 根据一个SWMM可视对象的ID，创建用于Sensor、Thing、FeatureOfInterest等一对一映射关系的SensorThings名称
	 *
	 * @param object 传入SWMM可视对象
	 * @return 名称
	 */
	public static String generateObjectName(VisualObject object) {
		return object.getId();
	}

	/**
	 * 根据一个SWMM可视对象的ID，创建用于ObservedProperty、Datastream等一对多映射关系的SensorThings名称
	 *
	 * @param object       传入SWMM可视对象
	 * @param propertyName SensorThings对象对应的可视对象的属性名
	 * @return 名称
	 */
	public static String generateObservedPropertyName(VisualObject object, String propertyName) {
		return String.format("%s-%s", object.getId(), propertyName);
	}

}
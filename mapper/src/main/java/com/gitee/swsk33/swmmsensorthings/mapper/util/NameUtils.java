package com.gitee.swsk33.swmmsensorthings.mapper.util;

import io.github.swsk33.swmmjava.model.VisualObject;

/**
 * 用于对SensorThings对象统一命名的实用类<br>
 * 命名规则：
 * <table border="1" cellpadding="8">
 *   <tr>
 *     <th>SWMM对象</th>
 *     <th>SensorThings对象类型</th>
 *     <th>对应关系</th>
 *     <th>SensorThings对象命名</th>
 *   </tr>
 *   <tr>
 *     <td>任意</td>
 *     <td>Sensor, Thing, FeatureOfInterest</td>
 *     <td>一对一</td>
 *     <td>SWMM对象类型-SWMM对象名称</td>
 *   </tr>
 *    <tr>
 *       <td>任意</td>
 *       <td>ObservedProperty、Datastream</td>
 *       <td>一对多</td>
 *      <td>SWMM对象类型-SWMM对象名称-SWMM属性名称</td>
 *    </tr>
 * </table>
 */
public class NameUtils {

	/**
	 * 根据一个SWMM可视对象的ID，创建用于Sensor、Thing、FeatureOfInterest等一对一映射关系的SensorThings名称
	 *
	 * @param object 传入SWMM可视对象
	 * @return 名称
	 */
	public static String generateObjectName(VisualObject object) {
		return String.format("%s-%s", object.getClass().getSimpleName(), object.getId());
	}

	/**
	 * 根据一个SWMM可视对象的ID，创建用于ObservedProperty、Datastream等一对多映射关系的SensorThings名称
	 *
	 * @param object       传入SWMM可视对象
	 * @param propertyName SensorThings对象对应的可视对象的属性名
	 * @return 名称，格式：对象类型-对象名称-属性名称
	 */
	public static String generateObservedPropertyName(VisualObject object, String propertyName) {
		return String.format("%s-%s-%s", object.getClass().getSimpleName(), object.getId(), propertyName);
	}

	/**
	 * 根据Datastream的名称，截取其对应的传感器名称
	 *
	 * @param datastreamName Datastream名称，为："对象类型-传感器名-观测属性名"形式
	 * @return 对应传感器名称
	 */
	public static String getObservationSensorName(String datastreamName) {
		return datastreamName.split("-")[1];
	}

	/**
	 * 根据Datastream的名称，截取其对应的观测属性名称
	 *
	 * @param datastreamName Datastream名称，为："对象类型-传感器名-观测属性名"形式
	 * @return 对应观测属性名称
	 */
	public static String getObservationSensorPropertyName(String datastreamName) {
		return datastreamName.split("-")[2];
	}

}
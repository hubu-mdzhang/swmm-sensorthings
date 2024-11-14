package com.gitee.swsk33.swmmsensorthings.mapper.factory;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.SensorCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.ThingCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Datastream;
import com.gitee.swsk33.swmmsensorthings.model.ObservedProperty;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 创建数据流对象的简单工厂
 */
@Slf4j
public class DatastreamFactory {

	/**
	 * 根据给定的SWMM可视对象以及指定的属性，创建一个关于该计算属性的数据流对象，将会同时创建并关联对应的观测属性，并创建Sensor和Thing进行关联<br>
	 * 若object为RainGage类型，则不会创建Thing，需要后续手动关联其对应的Subcatchment代表的Thing
	 *
	 * @param object SWMM可视对象
	 * @param name   可视对象的计算属性名称
	 * @return 创建的单个数据流对象，出现错误返回null
	 */
	public static Datastream createDatastream(VisualObject object, String name) {
		// 如果是雨量计，则仅创建为Sensor
		if (RainGage.class.isAssignableFrom(object.getClass())) {
			return createDatastream(object, name, SensorCreateStrategyContext.doCreateSensor(object), null);
		}
		// 否则，创建为Sensor和Thing
		return createDatastream(object, name, SensorCreateStrategyContext.doCreateSensor(object), ThingCreateStrategyContext.doCreateThing(object));
	}

	/**
	 * 根据给定的SWMM可视对象以及指定的属性，创建一个关于该计算属性的数据流对象，将会同时创建并关联对应的观测属性，并关联给定的Sensor和Thing
	 *
	 * @param object SWMM可视对象
	 * @param name   可视对象的计算属性名称
	 * @param sensor 给定传感器对象
	 * @param thing  给定实体对象
	 * @return 创建的单个数据流对象，出现错误返回null
	 */
	public static Datastream createDatastream(VisualObject object, String name, Sensor sensor, Thing thing) {
		// 先创建观测属性
		ObservedProperty property = new ObservedProperty();
		property.setName(String.format("%s-%s", object.getId(), name));
		property.setDescription("The observed property of " + object.getId());
		property.setDefinition(object.getClass().getName() + "." + name);
		// 创建数据流
		Datastream datastream = new Datastream();
		datastream.setName(String.format("%s-%s", object.getId(), name));
		datastream.setDescription("The datastream of " + object.getId() + "which contains observation record about the property " + name + ".");
		datastream.setObservationType(object.getClass().getName());
		datastream.setUnitOfMeasurement(new JSONObject());
		// 关联对象
		datastream.setThing(thing);
		datastream.setSensor(sensor);
		datastream.setObservedProperty(property);
		return datastream;
	}

	/**
	 * 根据可视对象的计算属性，对其所有计算属性创建对应的数据流对象，此外，还会同时根据可视对象创建对应的Thing和Sensor对象，具体：
	 * <ul>
	 *     <li>若传入RainGage，则在创建数据流的同时还会创建为具体Sensor并关联，但是不会创建为Thing，需要后续指定这个数据流中，该RainGage对应的Subcatchment代表的Thing</li>
	 *     <li>若传入Subcatchment、Link和Node，则在创建数据流的同时还会创建虚拟Sensor与Thing并进行关联</li>
	 * </ul>
	 *
	 * @param object SWMM可视对象
	 * @return 创建的数据流列表
	 */
	public static List<Datastream> createDatastreamList(VisualObject object) {
		// 如果是雨量计，则仅创建为Sensor
		if (RainGage.class.isAssignableFrom(object.getClass())) {
			return createDatastreamList(object, SensorCreateStrategyContext.doCreateSensor(object), null);
		}
		// 否则，创建为Sensor和Thing
		return createDatastreamList(object, SensorCreateStrategyContext.doCreateSensor(object), ThingCreateStrategyContext.doCreateThing(object));
	}

	/**
	 * 根据SWMM可视对象计算属性，对其所有计算属性创建对应的数据流对象，与此同时会创建观测属性对应并关联数据流，此外，还关联现有给定的Sensor和Thing对象
	 *
	 * @param object SWMM可视对象
	 * @param sensor 要关联的现有传感器对象
	 * @param thing  要关联的现有实体对象
	 * @return 创建的数据流列表
	 */
	public static List<Datastream> createDatastreamList(VisualObject object, Sensor sensor, Thing thing) {
		List<Datastream> result = new ArrayList<>();
		try {
			// 获取全部计算属性名称
			Set<String> propertyNames = PropertyReadUtils.getComputedPropertyNames(object);
			// 创建对象数据流
			for (String name : propertyNames) {
				result.add(createDatastream(object, name, sensor, thing));
			}
		} catch (Exception e) {
			log.error("读取对象计算属性名称时出现错误！");
			log.error(e.getMessage());
		}
		return result;
	}

}
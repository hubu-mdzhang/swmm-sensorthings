package com.gitee.swsk33.swmmsensorthings.mapper.factory;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.builder.ObjectFactoryBuilder;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.SensorCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.ThingCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.*;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 将对象映射为传感器数据的简单工厂
 */
@Slf4j
public class SensorDataFactory {

	/**
	 * 根据SWMM对象的计算属性数据，创建观测属性对象
	 *
	 * @param object SWMM可视对象
	 * @return 创建的观测属性队列列表，出现错误返回空列表
	 */
	private static List<ObservedProperty> createObservedProperties(VisualObject object) {
		List<ObservedProperty> observedProperties = new ArrayList<>();
		// 获取计算属性
		try {
			JSONObject computedProperties = PropertyReadUtils.readComputedProperties(object);
			for (String key : computedProperties.keySet()) {
				ObservedProperty observedProperty = new ObservedProperty();
				observedProperty.setName(key);
				observedProperty.setDescription("The observed property " + key + " of " + object.getId() + ".");
				observedProperty.setDefinition(object.getClass().getName());
				observedProperties.add(observedProperty);
			}
		} catch (Exception e) {
			log.error("创建观测属性时，获取{}的计算属性出错！类型：{}", object.getId(), object.getClass().getName());
			log.error(e.getMessage());
		}
		return observedProperties;
	}

	/**
	 * 根据SWMM对象的计算数据，创建观测对象，其中所有的其它关联属性包括传感器、实体、兴趣要素等，都调用工厂方法创建并完成关联
	 *
	 * @param object SWMM可视对象
	 * @param time   观测值对应的时间
	 * @return 映射后的观测对象，出现错误返回空列表
	 */
	public static List<Observation> createObservations(VisualObject object, LocalDateTime time) {
		SensorThingsObjectFactory factory = ObjectFactoryBuilder.getObjectFactory(object);
		// 创建传感器列表
		Map<String, Sensor> sensorMap = SensorCreateStrategyContext.doCreateSensors(object);
		// 如果不是RainGage对象，则创建Thing
		Thing thing;
		if (!RainGage.class.isAssignableFrom(object.getClass())) {
			thing = ThingCreateStrategyContext.doCreateThing(object);
		} else {
			// 否则，为其创建一个空的占位符实体
			thing = new Thing();
			thing.setName(object.getId());
			thing.setDescription("The placeholder thing of rain gage.");
		}
		// 如果是Subcatchment对象，则还创建兴趣要素
		FeatureOfInterest feature = null;
		if (Subcatchment.class.isAssignableFrom(object.getClass()) && factory != null) {
			feature = (FeatureOfInterest) factory.createObject(object).getFirst();
		}
		// 创建观测列表
		return createObservation(object, sensorMap, thing, feature, time);
	}

	/**
	 * 根据SWMM对象的计算数据，创建观测对象，关联现有的传感器、实体和兴趣要素，其中每一个观测属性对应一个传感器
	 *
	 * @param object    SWMM可视对象
	 * @param sensorMap 对应的现有传感器的哈希表
	 * @param thing     对应现有的实体
	 * @param feature   对应现有的兴趣要素
	 * @param time      观测值对应的时间
	 * @return 映射后的观测对象，出现错误返回空列表
	 */
	public static List<Observation> createObservation(VisualObject object, Map<String, Sensor> sensorMap, Thing thing, FeatureOfInterest feature, LocalDateTime time) {
		List<Observation> observations = new ArrayList<>();
		// 创建对象的观测属性列表
		List<ObservedProperty> observedProperties = createObservedProperties(object);
		// 获取计算属性，并创建为数据流与观测值
		try {
			JSONObject computedProperties = PropertyReadUtils.readComputedProperties(object);
			for (ObservedProperty eachProperty : observedProperties) {
				// 创建对应的数据流对象
				Datastream datastream = new Datastream();
				// 完善数据流基本信息
				datastream.setName(eachProperty.getName());
				datastream.setDescription("The datastream of observed property " + eachProperty.getName() + " of " + object.getId() + ".");
				datastream.setObservationType("");
				datastream.setUnitOfMeasurement(new JSONObject());
				// 设定数据流对象关联
				datastream.setThing(thing);
				datastream.setSensor(sensorMap.get(eachProperty.getName()));
				datastream.setObservedProperty(eachProperty);
				// 创建对应的观测对象
				Observation observation = new Observation();
				// 设定观测对象基本信息
				observation.setName(eachProperty.getName());
				observation.setDescription("The observation record of property " + eachProperty.getName() + " of " + object.getId() + ", which happens at " + time.toString() + ".");
				observation.setPhenomenonTime(time);
				observation.setResultTime(time);
				observation.setResult(computedProperties.get(eachProperty.getName()));
				// 设定观测值对象关联
				observation.setDatastream(datastream);
				observation.setFeatureOfInterest(feature);
				observations.add(observation);
			}
		} catch (Exception e) {
			log.error("创建观测值时，获取{}的计算属性出错！类型：{}", object.getId(), object.getClass().getName());
			log.error(e.getMessage());
		}
		return observations;
	}

}
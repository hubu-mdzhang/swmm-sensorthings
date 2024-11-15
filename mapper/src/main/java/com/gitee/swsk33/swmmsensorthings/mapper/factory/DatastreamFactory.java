package com.gitee.swsk33.swmmsensorthings.mapper.factory;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.SensorFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.ThingFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.SensorCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.ThingCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.*;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType.GEO_JSON;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.GeometryUtils.geometryToGeoJSON;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils.generateObservedPropertyName;

/**
 * 创建数据流对象的简单工厂
 */
@Slf4j
public class DatastreamFactory {

	/**
	 * 几何工厂对象
	 */
	private static final GeometryFactory geometryFactory = new GeometryFactory();

	/**
	 * 为雨量计对象创建占位符Thing
	 *
	 * @param object 雨量计对象
	 * @return 占位符作用的Thing
	 */
	private static Thing createPlaceholderThing(VisualObject object) {
		Thing placeholderThing = new Thing();
		placeholderThing.setName("RainGage Placeholder for " + object.getId());
		placeholderThing.setDescription("The thing which is a placeholder about the rain gage " + object.getId() + ".");
		Location location = new Location();
		location.setName("Placeholder Location for " + object.getId());
		location.setDescription("The location which is a placeholder about the rain gage " + object.getId() + ".");
		location.setEncodingType(GEO_JSON);
		location.setLocation(geometryToGeoJSON(geometryFactory.createPoint(new Coordinate(object.getCoordinate().getX(), object.getCoordinate().getY()))));
		placeholderThing.setLocations(Collections.singletonList(location));
		return placeholderThing;
	}

	/**
	 * 根据给定的SWMM可视对象以及指定的属性，创建一个关于该计算属性的数据流对象，将会同时创建并关联对应的观测属性，并创建Sensor和Thing进行关联<br>
	 * 若object为RainGage类型，则会创建一个无意义的占位符Thing，可后续手动关联其对应的Subcatchment代表的Thing
	 *
	 * @param object SWMM可视对象
	 * @param name   可视对象的计算属性名称
	 * @return 创建的单个数据流对象，出现错误返回null
	 */
	public static Datastream createDatastream(VisualObject object, String name) {
		// 如果是雨量计，则仅创建为Sensor
		if (RainGage.class.isAssignableFrom(object.getClass())) {
			return createDatastream(object, name, SensorCreateStrategyContext.doCreateSensor(object), createPlaceholderThing(object));
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
		property.setName(generateObservedPropertyName(object, name));
		property.setDescription("The observed property of " + object.getId());
		property.setDefinition(object.getClass().getName() + "." + name);
		// 创建数据流
		Datastream datastream = new Datastream();
		datastream.setName(generateObservedPropertyName(object, name));
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
	 * 根据可视对象的计算属性，对其所有计算属性创建对应的数据流对象，此外，还会同时根据可视对象创建对应的Thing和Sensor对象，具体来说：
	 * <ul>
	 *     <li>若传入RainGage，则在创建数据流的同时还会创建为具体Sensor并关联，并且会创建一个无意义的占位符Thing，可在后续指定这个数据流中该RainGage对应的Subcatchment代表的Thing</li>
	 *     <li>若传入Subcatchment、Link和Node，则在创建数据流的同时还会创建虚拟Sensor与Thing并进行关联</li>
	 * </ul>
	 *
	 * @param object SWMM可视对象
	 * @return 创建的数据流列表
	 */
	public static List<Datastream> createDatastreamList(VisualObject object) {
		// 如果是雨量计，则仅创建为Sensor
		if (RainGage.class.isAssignableFrom(object.getClass())) {
			return createDatastreamList(object, (Sensor) SensorFactory.getInstance().createObject(object), createPlaceholderThing(object));
		}
		// 否则，创建为Sensor和Thing
		return createDatastreamList(object, (Sensor) SensorFactory.getInstance().createObject(object), (Thing) ThingFactory.getInstance().createObject(object));
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
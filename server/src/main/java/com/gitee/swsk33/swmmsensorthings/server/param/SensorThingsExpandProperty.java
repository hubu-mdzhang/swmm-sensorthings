package com.gitee.swsk33.swmmsensorthings.server.param;

import com.gitee.swsk33.swmmsensorthings.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 存放不同类型SensorThings对象对应的展开字段列表常量
 */
public class SensorThingsExpandProperty {

	/**
	 * 存放不同类型SensorThings对象对应的展开字段数组
	 */
	private static final Map<Class<? extends SensorThingsObject>, String[]> EXPAND_PROPERTY_MAP = new HashMap<>();

	static {
		// 初始化列表
		EXPAND_PROPERTY_MAP.put(Datastream.class, new String[]{"ObservedProperty", "Sensor", "Thing", "Observations"});
		EXPAND_PROPERTY_MAP.put(FeatureOfInterest.class, new String[]{"Observations"});
		EXPAND_PROPERTY_MAP.put(Observation.class, new String[]{"Datastream", "FeatureOfInterest"});
		EXPAND_PROPERTY_MAP.put(ObservedProperty.class, new String[]{"Datastreams"});
		EXPAND_PROPERTY_MAP.put(Sensor.class, new String[]{"Datastreams"});
		EXPAND_PROPERTY_MAP.put(Thing.class, new String[]{"Locations", "Datastreams"});
	}

	/**
	 * 根据请求的对象类型，获取该对象的展开字段列表
	 *
	 * @param type SensorThings对象具体类型
	 * @return 展开字段列表副本
	 */
	public static String[] getExpandProperty(Class<? extends SensorThingsObject> type) {
		return Arrays.copyOf(EXPAND_PROPERTY_MAP.get(type), EXPAND_PROPERTY_MAP.get(type).length);
	}

}
package com.gitee.swsk33.swmmsensorthings.server.param;

import com.gitee.swsk33.swmmsensorthings.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * SensorThings API不同数据集的请求前缀
 */
public class SensorThingsPrefix {

	/**
	 * 实体
	 */
	public static final String THING = "/Things";

	/**
	 * 传感器
	 */
	public static final String SENSOR = "/Sensors";

	/**
	 * 观测属性
	 */
	public static final String OBSERVED_PROPERTY = "/ObservedProperties";

	/**
	 * 观测值
	 */
	public static final String OBSERVATION = "/Observations";

	/**
	 * 地点
	 */
	public static final String LOCATION = "/Locations";

	/**
	 * 历史地点
	 */
	public static final String HISTORICAL_LOCATION = "/HistoricalLocations";

	/**
	 * 兴趣要素
	 */
	public static final String FEATURES_OF_INTEREST = "/FeaturesOfInterest";

	/**
	 * 数据流
	 */
	public static final String DATASTREAM = "/Datastreams";

	/**
	 * 存放不同的Java实体类型对应的SensorThings API请求前缀的路径列表
	 */
	private static final Map<Class<? extends SensorThingsObject>, String> TYPE_PREFIX_MAP = new HashMap<>();

	static {
		TYPE_PREFIX_MAP.put(Thing.class, THING);
		TYPE_PREFIX_MAP.put(Sensor.class, SENSOR);
		TYPE_PREFIX_MAP.put(ObservedProperty.class, OBSERVED_PROPERTY);
		TYPE_PREFIX_MAP.put(Observation.class, OBSERVATION);
		TYPE_PREFIX_MAP.put(Location.class, LOCATION);
		TYPE_PREFIX_MAP.put(HistoricalLocation.class, HISTORICAL_LOCATION);
		TYPE_PREFIX_MAP.put(FeatureOfInterest.class, FEATURES_OF_INTEREST);
		TYPE_PREFIX_MAP.put(Datastream.class, DATASTREAM);
	}

	/**
	 * 获取对应SensorThings类型对应的请求路径前缀
	 *
	 * @param type SensorThings具体类型
	 * @return 对应的请求前缀
	 */
	public static String getTypePrefix(Class<? extends SensorThingsObject> type) {
		return TYPE_PREFIX_MAP.get(type);
	}

}
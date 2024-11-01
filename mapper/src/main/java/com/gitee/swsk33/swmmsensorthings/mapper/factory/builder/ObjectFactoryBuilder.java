package com.gitee.swsk33.swmmsensorthings.mapper.factory.builder;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.FeatureOfInterestFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.SensorFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.impl.ThingFactory;
import io.github.swsk33.swmmjava.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放SWMM转换到SensorThings的工厂实例的全局建造类型
 */
@Slf4j
public class ObjectFactoryBuilder {

	/**
	 * 存放具体的SWMM对象类型对应的转换工厂实例
	 */
	private static final Map<Class<? extends VisualObject>, SensorThingsObjectFactory> OBJECT_FACTORY_MAP = new HashMap();

	static {
		// 初始化工厂
		OBJECT_FACTORY_MAP.put(RainGage.class, new SensorFactory());
		OBJECT_FACTORY_MAP.put(Subcatchment.class, new FeatureOfInterestFactory());
		ThingFactory thingFactory = new ThingFactory();
		OBJECT_FACTORY_MAP.put(Link.class, thingFactory);
		OBJECT_FACTORY_MAP.put(Node.class, thingFactory);
	}

	/**
	 * 根据对应SWMM对象具体类型，获取对应的转换工厂
	 *
	 * @param visualObject SWMM可视对象实例，会根据实例判断对应工厂
	 * @return 对应转换工厂对象
	 */
	public static SensorThingsObjectFactory getObjectFactory(VisualObject visualObject) {
		if (RainGage.class.isAssignableFrom(visualObject.getClass())) {
			return OBJECT_FACTORY_MAP.get(RainGage.class);
		}
		if (Subcatchment.class.isAssignableFrom(visualObject.getClass())) {
			return OBJECT_FACTORY_MAP.get(Subcatchment.class);
		}
		if (Link.class.isAssignableFrom(visualObject.getClass())) {
			return OBJECT_FACTORY_MAP.get(Link.class);
		}
		if (Node.class.isAssignableFrom(visualObject.getClass())) {
			return OBJECT_FACTORY_MAP.get(Node.class);
		}
		log.error("不存在该类型的转换工厂：{}", visualObject.getClass().getName());
		return null;
	}

}
package com.gitee.swsk33.swmmsensorthings.mapper.strategy.context;

import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.GageToSensorStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.LinkToSensorStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.NodeToSensorStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.SubcatchmentToSensorStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 传感器创建策略上下文
 */
@Slf4j
public class SensorCreateStrategyContext {

	/**
	 * 存放全部策略的列表
	 */
	private static final Map<Class<? extends VisualObject>, SensorCreateStrategy> STRATEGY_MAP = new HashMap<>();

	static {
		// 初始化策略
		STRATEGY_MAP.put(RainGage.class, new GageToSensorStrategy());
		STRATEGY_MAP.put(Subcatchment.class, new SubcatchmentToSensorStrategy());
		STRATEGY_MAP.put(Link.class, new LinkToSensorStrategy());
		STRATEGY_MAP.put(Node.class, new NodeToSensorStrategy());
	}

	/**
	 * 调用不同策略，创建对应的传感器
	 *
	 * @param object 传入SWMM可视对象
	 * @return 转换后的传感器对象散列表，出现错误返回null
	 */
	public static Map<String, Sensor> doCreateSensors(VisualObject object) {
		if (object == null) {
			log.error("传入对象为空！");
			return null;
		}
		// 类型存在则直接获取策略
		if (STRATEGY_MAP.containsKey(object.getClass())) {
			return STRATEGY_MAP.get(object.getClass()).createSensors(object);
		}
		// 否则，判断是否是Link或者Node子类
		if (Link.class.isAssignableFrom(object.getClass())) {
			return STRATEGY_MAP.get(Link.class).createSensors(object);
		}
		if (Node.class.isAssignableFrom(object.getClass())) {
			return STRATEGY_MAP.get(Node.class).createSensors(object);
		}
		log.error("不存在对应的传感器映射策略！传入对象类型：{}", object.getClass());
		return null;
	}

}
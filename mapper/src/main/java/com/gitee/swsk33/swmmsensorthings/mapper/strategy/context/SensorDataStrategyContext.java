package com.gitee.swsk33.swmmsensorthings.mapper.strategy.context;

import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorDataStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.LinkMapStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.NodeMapStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.RainGageMapStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.SubcatchmentMapStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Observation;
import com.gitee.swsk33.swmmsensorthings.model.ObservedProperty;
import io.github.swsk33.swmmjava.model.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SWMM可视对象到传感器数据转换的全局策略
 */
@Slf4j
public class SensorDataStrategyContext {

	/**
	 * 存放全部不同的水文对象类型对应的SensorThings数据转换策略
	 */
	private static final Map<Class<? extends VisualObject>, SensorDataStrategy> STRATEGY_MAP = new HashMap<>();

	static {
		// 初始化策略列表
		STRATEGY_MAP.put(RainGage.class, new RainGageMapStrategy());
		STRATEGY_MAP.put(Subcatchment.class, new SubcatchmentMapStrategy());
		STRATEGY_MAP.put(Link.class, new LinkMapStrategy());
		STRATEGY_MAP.put(Node.class, new NodeMapStrategy());
	}

	/**
	 * 根据可视对象类型获取策略对象
	 *
	 * @param object 可视对象实例
	 * @return 对应策略对象
	 */
	private static SensorDataStrategy getStrategy(VisualObject object) {
		// 如果策略列表不包含对应策略，则进一步地判断是Link还是Node
		if (!STRATEGY_MAP.containsKey(object.getClass())) {
			if (Link.class.isAssignableFrom(object.getClass())) {
				return STRATEGY_MAP.get(Link.class);
			}
			if (Node.class.isAssignableFrom(object.getClass())) {
				return STRATEGY_MAP.get(Node.class);
			}
			log.error("不存在{}类型对应的转换策略！", object.getClass().getName());
			return null;
		}
		return STRATEGY_MAP.get(object.getClass());
	}

	/**
	 * 将一个可视对象转换成对应的SensorThings观测数据列表
	 *
	 * @param object 可视对象实例
	 * @param time   数据对应的时间
	 * @return SensorThings观测数据列表
	 */
	public static List<Observation> doMapObservation(VisualObject object, LocalDateTime time) {
		SensorDataStrategy strategy = getStrategy(object);
		if (strategy == null) {
			return null;
		}
		return strategy.mapObservation(object, time);
	}

	/**
	 * 将一个可视对象转换成对应的SensorThings观测属性列表
	 *
	 * @param object 可视对象实例
	 * @return SensorThings观测属性列表
	 */
	public static List<ObservedProperty> doMapObservedProperty(VisualObject object) {
		SensorDataStrategy strategy = getStrategy(object);
		if (strategy == null) {
			return null;
		}
		return strategy.mapObservedProperty(object);
	}

}
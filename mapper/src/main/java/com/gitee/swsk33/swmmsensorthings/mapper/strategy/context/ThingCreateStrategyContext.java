package com.gitee.swsk33.swmmsensorthings.mapper.strategy.context;

import com.gitee.swsk33.swmmsensorthings.mapper.strategy.ThingCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.LinkToThingStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.NodeToThingStrategy;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl.SubcatchmentToThingStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体创建策略上下文
 */
@Slf4j
public class ThingCreateStrategyContext {

	/**
	 * 存放全部策略的列表
	 */
	private static final Map<Class<? extends VisualObject>, ThingCreateStrategy> STRATEGY_MAP = new HashMap<>();

	static {
		// 初始化策略
		STRATEGY_MAP.put(Subcatchment.class, new SubcatchmentToThingStrategy());
		STRATEGY_MAP.put(Link.class, new LinkToThingStrategy());
		STRATEGY_MAP.put(Node.class, new NodeToThingStrategy());
	}

	/**
	 * 调用不同策略，创建对应的实体对象
	 *
	 * @param object 传入SWMM可视对象
	 * @return 转换后的实体对象，出现错误返回null
	 */
	public static Thing doCreateThing(VisualObject object) {
		if (object == null) {
			log.error("传入对象为空！");
			return null;
		}
		// 类型存在则直接获取策略
		if (STRATEGY_MAP.containsKey(object.getClass())) {
			return STRATEGY_MAP.get(object.getClass()).createThing(object);
		}
		// 否则，判断是否是Link或者Node子类
		if (Link.class.isAssignableFrom(object.getClass())) {
			return STRATEGY_MAP.get(Link.class).createThing(object);
		}
		if (Node.class.isAssignableFrom(object.getClass())) {
			return STRATEGY_MAP.get(Node.class).createThing(object);
		}
		log.error("不存在对应的实体映射策略！传入对象类型：{}", object.getClass());
		return null;
	}

}
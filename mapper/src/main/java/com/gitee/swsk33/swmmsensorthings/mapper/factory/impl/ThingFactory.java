package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.ThingCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 将SWMM对象转换成物体的具体工厂方法
 */
@Slf4j
public class ThingFactory implements SensorThingsObjectFactory {

	/**
	 * 实体工厂唯一单例
	 */
	private static volatile SensorThingsObjectFactory INSTANCE;

	/**
	 * 单例模式
	 */
	private ThingFactory() {

	}

	/**
	 * 获取实体工厂唯一单例
	 *
	 * @return 实体工厂单例
	 */
	public static SensorThingsObjectFactory getInstance() {
		if (INSTANCE == null) {
			synchronized (ThingFactory.class) {
				if (INSTANCE == null) {
					INSTANCE = new ThingFactory();
				}
			}
		}
		return INSTANCE;
	}

	@Override
	public SensorThingsObject createObject(VisualObject object) {
		if (object == null || (!Subcatchment.class.isAssignableFrom(object.getClass()) && !Node.class.isAssignableFrom(object.getClass()) && !Link.class.isAssignableFrom(object.getClass()))) {
			log.error("传入对象为空或类型不正确！需要：{}或者{}", Node.class.getName(), Link.class.getName());
			return null;
		}
		// Thing可以由除了RainGage之外的任何对象创建
		// 调用策略模式
		return ThingCreateStrategyContext.doCreateThing(object);
	}

}
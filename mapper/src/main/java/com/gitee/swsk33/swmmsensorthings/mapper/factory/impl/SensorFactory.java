package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.SensorCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 将SWMM对象构造为Sensor对象的具体工厂方法
 */
@Slf4j
public class SensorFactory implements SensorThingsObjectFactory {

	/**
	 * 传感器工厂唯一单例
	 */
	private static volatile SensorThingsObjectFactory INSTANCE;

	/**
	 * 单例模式
	 */
	private SensorFactory() {

	}

	/**
	 * 获取传感器工厂唯一单例
	 *
	 * @return 传感器工厂单例
	 */
	public static SensorThingsObjectFactory getInstance() {
		if (INSTANCE == null) {
			synchronized (SensorFactory.class) {
				if (INSTANCE == null) {
					INSTANCE = new SensorFactory();
				}
			}
		}
		return INSTANCE;
	}

	@Override
	public SensorThingsObject createObject(VisualObject object) {
		if (object == null) {
			log.error("传入对象为空！");
			return null;
		}
		// Sensor可以由全部SWMM对象创建，可能是基于观测属性的虚拟Sensor，也可以是具体意义的Sensor
		// 调用Sensor策略模式创建
		return SensorCreateStrategyContext.doCreateSensor(object);
	}

}
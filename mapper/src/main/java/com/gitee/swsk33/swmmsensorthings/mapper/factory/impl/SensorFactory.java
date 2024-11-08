package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.context.SensorCreateStrategyContext;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * 将雨量计对象构造为Sensor对象的具体工厂方法
 */
@Slf4j
public class SensorFactory implements SensorThingsObjectFactory {

	@Override
	public List<SensorThingsObject> createObject(VisualObject object) {
		if (object == null) {
			log.error("传入对象为空！");
			return null;
		}
		// Sensor可以由全部SWMM对象创建，可能是基于观测属性的虚拟Sensor，也可以是具体意义的Sensor
		// 调用Sensor策略模式创建
		return Objects.requireNonNull(SensorCreateStrategyContext.doCreateSensors(object)).stream().map(item -> (SensorThingsObject) item).toList();
	}

}
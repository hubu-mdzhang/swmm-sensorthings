package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 将链接类型的计算属性映射成虚拟传感器的具体策略
 */
@Slf4j
public class LinkToSensorStrategy implements SensorCreateStrategy {

	@Override
	public Sensor createSensor(VisualObject object) {
		// 创建为虚拟传感器
		Sensor sensor = new Sensor();
		sensor.setName(object.getId() + " Sensor");
		sensor.setDescription("The virtual sensor of the link " + object.getId() + ".");
		sensor.setMetadata(object.getClass().getSimpleName());
		sensor.setEncodingType(EncodingType.JSON);
		return sensor;
	}

}
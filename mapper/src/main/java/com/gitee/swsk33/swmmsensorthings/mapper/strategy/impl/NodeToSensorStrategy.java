package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 将节点类型的计算属性映射成虚拟传感器的具体策略
 */
@Slf4j
public class NodeToSensorStrategy implements SensorCreateStrategy {

	@Override
	public Sensor createSensor(VisualObject object) {
		// 创建为虚拟传感器
		Sensor sensor = new Sensor();
		sensor.setName(object.getId() + " Node Sensor");
		sensor.setDescription("The virtual sensor of the node " + object.getId() + ".");
		sensor.setMetadata(object.getClass().getSimpleName());
		sensor.setEncodingType(EncodingType.JSON);
		// 设定附加属性
		JSONObject properties = new JSONObject();
		properties.put("class", object.getClass().getName());
		sensor.setProperties(properties);
		return sensor;
	}

}
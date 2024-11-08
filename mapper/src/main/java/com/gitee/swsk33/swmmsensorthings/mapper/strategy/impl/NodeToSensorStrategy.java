package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils.readComputedProperties;

/**
 * 将节点类型的计算属性映射成虚拟传感器的具体策略
 */
@Slf4j
public class NodeToSensorStrategy implements SensorCreateStrategy {

	@Override
	public List<Sensor> createSensors(VisualObject object) {
		List<Sensor> virtualSensors = new ArrayList<>();
		// 读取计算属性
		try {
			JSONObject computedProperties = readComputedProperties(object);
			// 提取名字
			for (String key : computedProperties.keySet()) {
				// 创建为虚拟传感器
				Sensor sensor = new Sensor();
				sensor.setName(object.getId() + "_" + key);
				sensor.setDescription("The virtual sensor of the node " + object.getId() + ", which observes the " + key + " property");
				sensor.setMetadata(Node.class.getSimpleName() + "." + key);
				sensor.setEncodingType(EncodingType.JSON);
				virtualSensors.add(sensor);
			}
		} catch (Exception e) {
			log.error("读取节点对象计算属性出错！");
			log.error(e.getMessage());
		}
		return virtualSensors;
	}

}
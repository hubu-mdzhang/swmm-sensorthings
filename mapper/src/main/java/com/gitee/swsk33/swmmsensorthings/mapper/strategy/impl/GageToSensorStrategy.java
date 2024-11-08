package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.SensorCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils.readIntrinsicProperties;

/**
 * 将雨量计映射为传感器的具体策略
 */
@Slf4j
public class GageToSensorStrategy implements SensorCreateStrategy {

	@Override
	public Map<String, Sensor> createSensors(VisualObject object) {
		RainGage rainGage = (RainGage) object;
		// 创建Sensor
		Sensor sensor = new Sensor();
		sensor.setName(rainGage.getId());
		sensor.setDescription("This is the concrete rain gage sensor " + rainGage.getId() + " of the SWMM system.");
		sensor.setMetadata(RainGage.class.getSimpleName());
		sensor.setEncodingType(EncodingType.JSON);
		// 读取固有属性
		try {
			sensor.setProperties(readIntrinsicProperties(rainGage));
		} catch (Exception e) {
			log.error("读取雨量计对象固有属性出错！");
			log.error(e.getMessage());
		}
		return Map.of("rainfall", sensor);
	}

}
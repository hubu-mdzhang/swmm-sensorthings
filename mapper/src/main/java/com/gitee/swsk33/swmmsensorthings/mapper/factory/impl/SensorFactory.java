package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Sensor;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import io.github.swsk33.swmmjava.model.RainGage;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 将雨量计对象构造为Sensor对象的具体工厂方法
 */
@Slf4j
public class SensorFactory implements SensorThingsObjectFactory {

	@Override
	public SensorThingsObject createObject(VisualObject object) {
		if (object == null || !RainGage.class.isAssignableFrom(object.getClass())) {
			log.error("传入对象为空！或者类型不正确！需要：{}", RainGage.class.getName());
			return null;
		}
		// 原始雨量计对象
		RainGage gage = (RainGage) object;
		// 构造传感器对象
		Sensor sensor = new Sensor();
		sensor.setName(gage.getId());
		try {
			// 追加属性
			JSONObject properties = PropertyReadUtils.readIntrinsicProperties(gage);
			sensor.setProperties(properties);
		} catch (Exception e) {
			log.error("读取固有属性时发生错误！");
			log.error(e.getMessage());
		}
		return sensor;
	}

}
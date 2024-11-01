package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Location;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;

import java.util.Collections;

/**
 * 将节点/链接转换成物体的具体工厂方法
 */
@Slf4j
public class ThingFactory implements SensorThingsObjectFactory {

	@Override
	public SensorThingsObject createObject(VisualObject object) {
		if (object == null || (!Node.class.isAssignableFrom(object.getClass()) && !Link.class.isAssignableFrom(object.getClass()))) {
			log.error("传入对象为空或类型不正确！需要：{}或者{}", Node.class.getName(), Link.class.getName());
			return null;
		}
		// 构造物品对象
		Thing thing = new Thing();
		thing.setName(object.getId());
		// 设定位置
		Location location = new Location();
		location.setLocation(new Coordinate(object.getCoordinate().getX(), object.getCoordinate().getY()));
		thing.setLocations(Collections.singletonList(location));
		try {
			// 追加属性
			JSONObject properties = PropertyReadUtils.readIntrinsicProperties(object);
			thing.setProperties(properties);
		} catch (Exception e) {
			log.error("读取固有属性时发生错误！");
			log.error(e.getMessage());
		}
		return thing;
	}

}
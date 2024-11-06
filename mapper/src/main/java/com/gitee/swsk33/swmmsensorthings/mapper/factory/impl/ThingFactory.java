package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.util.GeometryUtils;
import com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils;
import com.gitee.swsk33.swmmsensorthings.model.Location;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.util.Collections;

/**
 * 将节点/链接转换成物体的具体工厂方法
 */
@Slf4j
public class ThingFactory implements SensorThingsObjectFactory {

	/**
	 * 全部几何构造工厂
	 */
	private static final GeometryFactory geometryFactory = new GeometryFactory();

	@Override
	public SensorThingsObject createObject(VisualObject object) {
		if (object == null || (!Node.class.isAssignableFrom(object.getClass()) && !Link.class.isAssignableFrom(object.getClass()))) {
			log.error("传入对象为空或类型不正确！需要：{}或者{}", Node.class.getName(), Link.class.getName());
			return null;
		}
		// 构造物品对象
		Thing thing = new Thing();
		thing.setName(object.getId());
		// 根据节点或者链接类型，设定其地理位置信息
		Location location = new Location();
		if (Link.class.isAssignableFrom(object.getClass())) {
			Link link = (Link) object;
			// 链接类型的位置信息为一个线对象
			Coordinate start = new Coordinate(link.getUpstream().getCoordinate().getX(), link.getUpstream().getCoordinate().getY());
			Coordinate end = new Coordinate(link.getDownstream().getCoordinate().getX(), link.getDownstream().getCoordinate().getY());
			LineString line = geometryFactory.createLineString(new Coordinate[]{start, end});
			location.setLocation(GeometryUtils.geometryToGeoJSON(line));
			// 补充描述
			thing.setDescription("The Link " + link.getId() + " of SWMM.");
		} else {
			// 节点类型的位置信息为一个点对象
			location.setLocation(GeometryUtils.geometryToGeoJSON(geometryFactory.createPoint(new Coordinate(object.getCoordinate().getX(), object.getCoordinate().getY()))));
			// 补充描述
			thing.setDescription("The Node " + object.getId() + " of SWMM.");
		}
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
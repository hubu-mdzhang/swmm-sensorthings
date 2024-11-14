package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.ThingCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Location;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.Link;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import java.util.Collections;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.GeometryUtils.geometryToGeoJSON;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils.readIntrinsicProperties;

/**
 * 链接类型对象映射成实体的具体策略
 */
@Slf4j
public class LinkToThingStrategy implements ThingCreateStrategy {

	@Override
	public Thing createThing(VisualObject object) {
		Link link = (Link) object;
		// 创建Thing
		Thing thing = new Thing();
		thing.setName(link.getId());
		thing.setDescription("The link " + link.getId() + " of SWMM System.");
		// 创建地理位置几何图形
		Coordinate[] startToEnd = new Coordinate[2];
		startToEnd[0] = new Coordinate(link.getUpstream().getCoordinate().getX(), link.getUpstream().getCoordinate().getY());
		startToEnd[1] = new Coordinate(link.getDownstream().getCoordinate().getX(), link.getDownstream().getCoordinate().getY());
		LineString line = geometryFactory.createLineString(startToEnd);
		// 创建地理位置信息
		Location location = new Location();
		location.setName("Location of " + link.getId());
		location.setDescription("The line string which represents the feature of " + link.getId() + ".");
		location.setEncodingType(EncodingType.GEO_JSON);
		location.setLocation(geometryToGeoJSON(line));
		// 关联位置信息
		thing.setLocations(Collections.singletonList(location));
		// 设定固有属性
		try {
			thing.setProperties(readIntrinsicProperties(link));
			thing.getProperties().put("class", object.getClass().getName());
		} catch (Exception e) {
			log.error("读取链接类型的固有属性出错！");
			log.error(e.getMessage());
		}
		return thing;
	}

}
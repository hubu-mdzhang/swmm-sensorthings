package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.ThingCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Location;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.Node;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import java.util.Collections;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.GeometryUtils.geometryToGeoJSON;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.NameUtils.generateObjectName;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils.readIntrinsicProperties;

/**
 * 将节点类型对象转换成实体的具体策略
 */
@Slf4j
public class NodeToThingStrategy implements ThingCreateStrategy {

	@Override
	public Thing createThing(VisualObject object) {
		Node node = (Node) object;
		// 创建Thing
		Thing thing = new Thing();
		thing.setName(generateObjectName(object));
		thing.setDescription("The node " + node.getId() + " of SWMM System.");
		// 创建地理要素
		Point point = geometryFactory.createPoint(new Coordinate(node.getCoordinate().getX(), node.getCoordinate().getY()));
		// 创建位置信息
		Location location = new Location();
		location.setName("Location of " + node.getId());
		location.setDescription("The location point of node " + node.getId() + ".");
		location.setEncodingType(EncodingType.GEO_JSON);
		location.setLocation(geometryToGeoJSON(point));
		// 关联位置信息
		thing.setLocations(Collections.singletonList(location));
		// 读取固有属性
		try {
			thing.setProperties(readIntrinsicProperties(node));
			thing.getProperties().put("class", object.getClass().getName());
		} catch (Exception e) {
			log.error("读取节点类型的固有属性出错！");
			log.error(e.getMessage());
		}
		return thing;
	}

}
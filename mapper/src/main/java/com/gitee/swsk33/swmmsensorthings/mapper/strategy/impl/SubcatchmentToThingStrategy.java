package com.gitee.swsk33.swmmsensorthings.mapper.strategy.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.mapper.strategy.ThingCreateStrategy;
import com.gitee.swsk33.swmmsensorthings.model.Location;
import com.gitee.swsk33.swmmsensorthings.model.Thing;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.Collections;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.GeometryUtils.geometryToGeoJSON;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils.readIntrinsicProperties;

/**
 * 将子汇水区域映射成实体的具体策略
 */
@Slf4j
public class SubcatchmentToThingStrategy implements ThingCreateStrategy {

	@Override
	public Thing createThing(VisualObject object) {
		Subcatchment catchment = (Subcatchment) object;
		// 创建Thing
		Thing thing = new Thing();
		thing.setName(catchment.getId());
		thing.setDescription("The subcatchment " + catchment.getId() + " of SWMM System.");
		// 构建地理位置几何图形
		Coordinate[] coordinates = new Coordinate[catchment.getPolygon().size() + 1];
		for (int i = 0; i < coordinates.length - 1; i++) {
			coordinates[i] = new Coordinate(catchment.getPolygon().get(i).getX(), catchment.getPolygon().get(i).getY());
		}
		coordinates[coordinates.length - 1] = new Coordinate(catchment.getPolygon().getFirst().getX(), catchment.getPolygon().getFirst().getY());
		Polygon polygon = geometryFactory.createPolygon(coordinates);
		// 创建地理位置对象
		Location location = new Location();
		location.setName("Initial Location of " + catchment.getId());
		location.setDescription("The initial location of subcatchment " + catchment.getId() + ".");
		location.setEncodingType(EncodingType.GEO_JSON);
		location.setLocation(geometryToGeoJSON(polygon));
		// 关联Thing
		thing.setLocations(Collections.singletonList(location));
		// 读取固有属性
		try {
			thing.setProperties(readIntrinsicProperties(catchment));
			thing.getProperties().put("class", Subcatchment.class.getName());
		} catch (Exception e) {
			log.error("读取子汇水区域的固有属性出错！");
			log.error(e.getMessage());
		}
		return thing;
	}

}
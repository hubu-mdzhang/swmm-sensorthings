package com.gitee.swsk33.swmmsensorthings.mapper.factory.impl;

import com.gitee.swsk33.swmmsensorthings.mapper.factory.SensorThingsObjectFactory;
import com.gitee.swsk33.swmmsensorthings.mapper.param.EncodingType;
import com.gitee.swsk33.swmmsensorthings.model.FeatureOfInterest;
import com.gitee.swsk33.swmmsensorthings.model.SensorThingsObject;
import io.github.swsk33.swmmjava.model.Subcatchment;
import io.github.swsk33.swmmjava.model.VisualObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import static com.gitee.swsk33.swmmsensorthings.mapper.util.GeometryUtils.geometryToGeoJSON;
import static com.gitee.swsk33.swmmsensorthings.mapper.util.PropertyReadUtils.readIntrinsicProperties;

/**
 * 将SWMM对象转换为兴趣要素的具体工厂方法
 */
@Slf4j
public class FeatureOfInterestFactory implements SensorThingsObjectFactory {

	/**
	 * 几何图形工厂对象
	 */
	private static final GeometryFactory geometryFactory = new GeometryFactory();

	/**
	 * 唯一单例
	 */
	private static volatile SensorThingsObjectFactory INSTANCE;

	/**
	 * 单例模式
	 */
	private FeatureOfInterestFactory() {

	}

	/**
	 * 获取兴趣要素工厂唯一单例
	 *
	 * @return 兴趣要素工厂单例
	 */
	public static SensorThingsObjectFactory getInstance() {
		if (INSTANCE == null) {
			synchronized (FeatureOfInterestFactory.class) {
				if (INSTANCE == null) {
					INSTANCE = new FeatureOfInterestFactory();
				}
			}
		}
		return INSTANCE;
	}

	@Override
	public SensorThingsObject createObject(VisualObject object) {
		if (object == null || !Subcatchment.class.isAssignableFrom(object.getClass())) {
			log.error("传入对象为空，或者类型不正确！需要类型：{}", Subcatchment.class.getName());
			return null;
		}
		// FeatureOfInterest仅从Subcatchment创建，为一对一关系
		// 原始子汇水区域对象
		Subcatchment catchment = (Subcatchment) object;
		// 构造兴趣要素对象
		FeatureOfInterest featureOfInterest = new FeatureOfInterest();
		featureOfInterest.setName(catchment.getId());
		featureOfInterest.setDescription("The feature of interest about subcatchment " + catchment.getId() + " in SWMM System.");
		featureOfInterest.setEncodingType(EncodingType.GEO_JSON);
		// 创建多边形
		Coordinate[] coordinates = new Coordinate[catchment.getPolygon().size() + 1];
		for (int i = 0; i < catchment.getPolygon().size(); i++) {
			coordinates[i] = new Coordinate(catchment.getPolygon().get(i).getX(), catchment.getPolygon().get(i).getY());
		}
		coordinates[coordinates.length - 1] = new Coordinate(catchment.getPolygon().getFirst().getX(), catchment.getPolygon().getFirst().getY());
		// 创建线串
		LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
		// 创建多边形
		Polygon polygon = geometryFactory.createPolygon(linearRing);
		featureOfInterest.setFeature(geometryToGeoJSON(polygon));
		try {
			// 追加属性
			featureOfInterest.setProperties(readIntrinsicProperties(catchment));
			featureOfInterest.getProperties().put("class", FeatureOfInterest.class.getName());
		} catch (Exception e) {
			log.error("读取固有属性时发生错误！");
			log.error(e.getMessage());
		}
		return featureOfInterest;
	}

}
package com.gitee.swsk33.swmmsensorthings.mapper.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

/**
 * 关于几何数据输入输出和转换处理的实用类
 */
@Slf4j
public class GeometryUtils {

	/**
	 * GeoJSON读取器对象
	 */
	private static final GeoJsonReader GEO_JSON_READER = new GeoJsonReader();

	/**
	 * GeoJSON写入器对象
	 */
	private static final GeoJsonWriter GEO_JSON_WRITER = new GeoJsonWriter();

	/**
	 * 通过GeoJSON字符串创建几何图形对象
	 *
	 * @param GeoJSON GeoJSON字符串
	 * @return 几何对象
	 */
	public static Geometry createGeometryFromGeoJSON(String GeoJSON) throws Exception {
		return GEO_JSON_READER.read(GeoJSON);
	}

	/**
	 * 将几何图形对象转换成GeoJSON字符串
	 *
	 * @param geometry 几何图形对象
	 * @return JSONObject对象
	 */
	public static JSONObject geometryToGeoJSON(Geometry geometry) {
		JSONObject geometryJson = JSON.parseObject(GEO_JSON_WRITER.write(geometry));
		geometryJson.remove("crs");
		return geometryJson;
	}

	/**
	 * 修复多边形类几何图形（如果几何图形无效的话）
	 *
	 * @param geometry 可能无效的几何图形
	 * @return 修复后的图形
	 */
	public static Geometry fixPolygon(Geometry geometry) {
		if (!geometry.isValid()) {
			return geometry.buffer(0);
		}
		return geometry;
	}

}
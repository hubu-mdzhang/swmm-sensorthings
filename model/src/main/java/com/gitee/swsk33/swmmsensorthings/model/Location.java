package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 表示一个地点
 */
@Data
@ToString(callSuper = true)
public class Location extends SensorThingsObject {

	/**
	 * 编码类型
	 */
	private String encodingType;

	/**
	 * 地点经纬度或者范围，使用GeoJSON表示
	 */
	private JSONObject location;

	/**
	 * 附加属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

	/**
	 * 这个地点关联的物品
	 */
	@JSONField(name = "Things")
	@SensorThingsOptional
	private List<Thing> things;

}
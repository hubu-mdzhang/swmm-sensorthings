package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 表示任何可以在互联网中被标识的实体
 */
@Data
@ToString(callSuper = true)
public class Thing extends SensorThingsObject {

	/**
	 * 额外属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

	/**
	 * 这个实体关联的地点，包括历史地点
	 */
	@JSONField(name = "Locations")
	@JsonProperty("Locations")
	@SensorThingsOptional
	private List<Location> locations;

	/**
	 * 这个实体关联的数据流列表
	 */
	@JSONField(name = "Datastreams")
	@JsonProperty("Datastreams")
	private List<Datastream> datastreams;

}
package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 兴趣要素
 */
@Data
@ToString(callSuper = true)
public class FeatureOfInterest extends SensorThingsObject {

	/**
	 * 编码类型
	 */
	private String encodingType;

	/**
	 * 要素，使用GeoJSON表示
	 */
	private JSONObject feature;

	/**
	 * 其它属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

	/**
	 * 该兴趣要素对应的观测值记录
	 */
	@JSONField(name = "Observations")
	@JsonProperty("Observations")
	private List<Observation> observations;

}
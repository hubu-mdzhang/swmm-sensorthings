package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

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

}
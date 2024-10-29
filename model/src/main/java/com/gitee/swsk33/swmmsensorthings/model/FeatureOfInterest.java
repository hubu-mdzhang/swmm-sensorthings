package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

/**
 * 兴趣要素
 */
@Data
public class FeatureOfInterest extends SensorThingsObject {

	/**
	 * 编码类型
	 */
	private String encodingType;

	/**
	 * 要素，可以是一个多边形
	 */
	private Object feature;

	/**
	 * 其它属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

}
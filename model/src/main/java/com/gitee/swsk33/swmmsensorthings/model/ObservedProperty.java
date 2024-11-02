package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

/**
 * 观测属性
 */
@Data
@ToString(callSuper = true)
public class ObservedProperty extends SensorThingsObject {

	/**
	 * 观测属性的结构定义，通常是一个Schema URL
	 */
	private String definition;

	/**
	 * 其它属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

}
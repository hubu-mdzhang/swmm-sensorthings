package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.MetaProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

/**
 * 观测属性
 */
@Data
public class ObservedProperty {

	/**
	 * 唯一标识
	 */
	@MetaProperty
	private Object id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 观测属性的结构定义，通常是一个Schema URL
	 */
	private String definition;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 其它属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

}
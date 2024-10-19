package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.MetaProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

/**
 * 传感器
 */
@Data
public class Sensor {

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
	 * 描述
	 */
	private String description;

	/**
	 * 编码格式
	 */
	private String encodingType;

	/**
	 * 相关元数据
	 */
	private Object metadata;

	/**
	 * 其它元数据
	 */
	@SensorThingsOptional
	private JSONObject properties;

}
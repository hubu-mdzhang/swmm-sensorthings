package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.annotation.MetaProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

import java.util.List;

/**
 * 表示一个地点
 */
@Data
public class Location {

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
	 * 编码类型
	 */
	private String encodingType;

	/**
	 * 地点经纬度或者范围
	 */
	private Object location;

	/**
	 * 附加属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

	/**
	 * 这个地点关联的物品
	 */
	@SensorThingsOptional
	private List<Thing> things;

}
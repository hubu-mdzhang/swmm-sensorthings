package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.util.List;

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

	/**
	 * 对应的数据流对象
	 */
	@JSONField(name = "Datastreams")
	@JsonProperty("Datastreams")
	private List<Datastream> datastreams;

}
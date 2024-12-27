package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 传感器
 */
@Data
@ToString(callSuper = true)
public class Sensor extends SensorThingsObject {

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

	/**
	 * 该传感器对应的数据流列表
	 */
	@JSONField(name = "Datastreams")
	@JsonProperty("Datastreams")
	private List<Datastream> datastreams;

}
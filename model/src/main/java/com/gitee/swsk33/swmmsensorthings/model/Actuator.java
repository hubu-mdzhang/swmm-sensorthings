package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

import java.util.List;

/**
 * SensorThings控制器对象
 */
@Data
public class Actuator extends SensorThingsObject {

	/**
	 * 编码类型
	 */
	private String encodingType;

	/**
	 * 元数据
	 */
	private Object metadata;

	/**
	 * 额外属性
	 */
	@SensorThingsOptional
	private JSONField properties;

	/**
	 * 包含的任务控制列表
	 */
	@JSONField(name = "TaskingCapabilities")
	@JsonProperty("TaskingCapabilities")
	private List<TaskingCapability> capabilities;

}
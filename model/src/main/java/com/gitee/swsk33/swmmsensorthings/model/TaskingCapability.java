package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.swsk33.swmmsensorthings.annotation.SensorThingsOptional;
import lombok.Data;

/**
 * SensorThings API任务描述
 */
@Data
public class TaskingCapability extends SensorThingsObject {

	/**
	 * 任务参数描述
	 */
	private JSONObject taskingParameters;

	/**
	 * 附加属性
	 */
	@SensorThingsOptional
	private JSONObject properties;

	/**
	 * 关联的Actuator
	 */
	@JSONField(name = "Actuator")
	@JsonProperty("Actuator")
	private Actuator actuator;

	/**
	 * 关联的Thing
	 */
	@JSONField(name = "Thing")
	@JsonProperty("Thing")
	private Thing thing;

}
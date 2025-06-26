package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SensorThings控制任务对象
 */
@Data
public class Task extends SensorThingsObject {

	/**
	 * 任务参数
	 */
	private JSONObject taskingParameters;

	/**
	 * 创建时间
	 */
	private LocalDateTime creationTime = LocalDateTime.now();

	/**
	 * 关联任务能力
	 */
	@JSONField(name = "TaskingCapability")
	@JsonProperty("TaskingCapability")
	private TaskingCapability capability;

}
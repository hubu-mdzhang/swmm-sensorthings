package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 历史地点
 */
@Data
@ToString(callSuper = true)
public class HistoricalLocation extends SensorThingsObject {

	/**
	 * 对应时间
	 */
	private LocalDateTime time;

	/**
	 * 对应物品
	 */
	@JSONField(name = "Thing")
	private Thing thing;

	/**
	 * 对应具体地点
	 */
	@JSONField(name = "Location")
	private Location location;

}
package com.gitee.swsk33.swmmsensorthings.model;

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
	private Thing thing;

	/**
	 * 对应具体地点
	 */
	private Location location;

}
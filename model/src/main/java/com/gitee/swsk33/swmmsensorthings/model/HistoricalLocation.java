package com.gitee.swsk33.swmmsensorthings.model;

import com.gitee.swsk33.swmmsensorthings.annotation.MetaProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 历史地点
 */
@Data
public class HistoricalLocation {

	/**
	 * 唯一标识
	 */
	@MetaProperty
	private Object id;

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
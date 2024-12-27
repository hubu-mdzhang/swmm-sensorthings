package com.gitee.swsk33.swmmsensorthings.mapper.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * 时间处理实用类
 */
public class TimeUtils {

	/**
	 * 把LocalDateTime转换成带时区偏移的时间对象
	 *
	 * @param localDateTime 本地时间对象
	 * @return 使用系统时区偏移的OffsetDateTime对象
	 */
	public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
		// 获取系统时区偏移
		ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
		return localDateTime.atOffset(offset);
	}

}
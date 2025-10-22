package com.gitee.swsk33.swmmsensorthings.eventbus.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理浮点数转换实用类
 */
@Slf4j
public class DoubleValueUtils {

	/**
	 * 将不确定的浮点数类型转换成double原始类型
	 *
	 * @param value 原始数据
	 * @return double类型
	 */
	public static double parseDouble(Object value) {
		if (value.getClass().isPrimitive() && double.class.isAssignableFrom(value.getClass())) {
			return (Double) value;
		}
		if (Number.class.isAssignableFrom(value.getClass())) {
			return ((Number) value).doubleValue();
		}
		if (String.class.isAssignableFrom(value.getClass())) {
			return Double.parseDouble((String) value);
		}
		log.warn("无法将{}类型解析为浮点数！", value.getClass().getName());
		return 0;
	}

}
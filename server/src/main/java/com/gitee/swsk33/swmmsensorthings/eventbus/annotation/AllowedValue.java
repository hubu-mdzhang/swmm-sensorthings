package com.gitee.swsk33.swmmsensorthings.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识API-Processes相关对象的类中，字段允许接收的值，字符串类型<br>
 * 通常标注在值为枚举的字段上，字段类型需要是字符串或者字符串数组，若未标注表示允许接收所有值
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValue {

	/**
	 * 被标注字段允许接收的值
	 */
	String[] value();

}
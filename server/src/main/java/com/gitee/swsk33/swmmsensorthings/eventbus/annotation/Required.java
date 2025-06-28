package com.gitee.swsk33.swmmsensorthings.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注API-Processes规范中，字段是否必须
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

	/**
	 * 被标注字段是否必须存在
	 */
	boolean value() default true;

}
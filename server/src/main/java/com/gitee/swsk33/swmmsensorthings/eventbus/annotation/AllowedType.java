package com.gitee.swsk33.swmmsensorthings.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;

/**
 * 用于标识API-Processes相关对象的类中，字段允许接收的类型<br>
 * 通常标注在Object类型字段，若未标注表示允许接收所有类型
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedType {

	/**
	 * 被标注字段允许接收的类型<br>
	 * <ul>
	 *     <li>当实际值类型是其中定义类型之一或者其中任意一个类型的子类时，则校验通过</li>
	 *     <li>此外，如果指定了{@link Array}作为类型，则会判断对应值是不是数组</li>
	 * </ul>
	 */
	Class<?>[] value();

}
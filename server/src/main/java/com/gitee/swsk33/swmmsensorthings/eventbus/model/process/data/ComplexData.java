package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.data;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedType;
import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.Required;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Format;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * 表示API-Processes中一个复杂参数值类型
 */
@Data
public class ComplexData extends Format {

	/**
	 * 参数的值，允许传递的类型
	 * <ul>
	 *     <li>字符串</li>
	 *     <li>数字</li>
	 *     <li>布尔值</li>
	 *     <li>数组</li>
	 *     <li>JSON对象</li>
	 *     <li>四至范围bbox对象</li>
	 * </ul>
	 */
	@Required
	@AllowedType({String.class, Number.class, Boolean.class, Array.class, List.class, Map.class, BoundBox.class})
	private Object value;

}
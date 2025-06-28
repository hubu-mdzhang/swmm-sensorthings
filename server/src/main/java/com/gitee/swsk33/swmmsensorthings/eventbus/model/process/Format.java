package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedType;
import lombok.Data;

import java.util.Map;

/**
 * API-Processes中描述数据格式的对象
 */
@Data
public class Format {

	/**
	 * 数据的MIME类型<br>
	 * 如：<code>application/json</code>
	 */
	private String mediaType;

	/**
	 * 数据的编码方式<br>
	 * 如：<code>base64</code>
	 */
	private String encoding;

	/**
	 * 数据的格式Schema，有两种情况
	 * <ul>
	 *  <li>引用传递：schema对象中有且只能有1个属性$ref，表示指向该参数校验规则的JSON Schema文件地址</li>
	 * 	<li>值传递：schema对象本身即为一个JSON Schema对象</li>
	 * </ul>
	 */
	@AllowedType({String.class, Map.class})
	private Object schema;

}
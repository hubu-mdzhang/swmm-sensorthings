package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.param;

import com.alibaba.fastjson2.JSONObject;
import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.ProcessesRequired;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Metadata;
import lombok.Data;

/**
 * API-Processes中关于进程/算子的参数描述（入参和出参）的抽象类
 */
@Data
public abstract class ParamDescription {

	/**
	 * 参数标题/显示名称
	 */
	private String title;

	/**
	 * 参数描述信息
	 */
	private String description;

	/**
	 * 参数关键词列表
	 */
	private String[] keywords;

	/**
	 * 参数额外元数据
	 */
	private Metadata[] metadata;

	/**
	 * 用于校验该参数的JSON Schema对象，支持使用引用或者值传递JSON Schema
	 * <ul>
	 *     <li>引用传递：schema对象中有且只能有1个属性$ref，表示指向该参数校验规则的JSON Schema文件地址</li>
	 *     <li>值传递：schema对象本身即为一个JSON Schema对象</li>
	 * </ul>
	 */
	@ProcessesRequired
	private JSONObject schema;

}
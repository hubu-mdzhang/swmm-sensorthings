package com.gitee.swsk33.swmmsensorthings.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.gitee.swsk33.swmmsensorthings.annotation.MetaProperty;
import lombok.Data;

/**
 * 表示SensorThings概念模型的抽象类型
 */
@Data
public abstract class SensorThingsObject {

	/**
	 * 主键id
	 */
	@JSONField(name = "@iot.id")
	@MetaProperty
	private Object id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

}
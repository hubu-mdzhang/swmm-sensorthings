package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import lombok.Data;

/**
 * API-Processes中元数据对象<br>
 * 用于补充说明的元数据信息，例如官方文档、数据格式定义、标准规范等
 */
@Data
public class Metadata {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 元数据的用途/角色/类型
	 */
	private String role;

	/**
	 * 内容引用/网址
	 */
	private String href;

}
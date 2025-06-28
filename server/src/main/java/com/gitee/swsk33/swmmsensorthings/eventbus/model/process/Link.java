package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.Required;
import lombok.Data;

/**
 * API-Processes中表示内容链接的对象<br>
 * 例如操作入口（执行端点）、文档链接等，允许客户端直接与服务交互或获取更多信息
 */
@Data
public class Link {

	/**
	 * 链接名称/标题
	 */
	private String title;

	/**
	 * 链接角色/类型/用途<br>
	 * 表示链接的关系类型（即该链接的用途），通常使用预定义的URI标识符
	 */
	private String rel;

	/**
	 * 链接内容
	 */
	@Required
	private String href;

	/**
	 * 表示目标资源的媒体类型（MIME类型）<br>
	 * 如：<code>application/json</code>
	 */
	private String type;

	/**
	 * 目标资源的语言<br>
	 * 如：<code>zh</code>
	 */
	private String hreflang;

}
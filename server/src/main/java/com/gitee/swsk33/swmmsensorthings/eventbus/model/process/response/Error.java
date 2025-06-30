package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.Required;
import lombok.Data;

/**
 * API-Processes中的错误响应
 */
@Data
public class Error {

	/**
	 * 错误类型，比如：
	 * <ul>
	 *     <li>Not Found</li>
	 *     <li>Server Error</li>
	 * </ul>
	 */
	@Required
	private String type;

	/**
	 * 错误标题
	 */
	private String title;

	/**
	 * 错误状态码
	 */
	private int status;

	/**
	 * 错误细节
	 */
	private String detail;

	/**
	 * 错误实例
	 */
	private String instance;

}
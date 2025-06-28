package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.param;

import lombok.Data;

/**
 * API-Processes中输入参数描述对象
 */
@Data
public class InputDescription extends ParamDescription {

	/**
	 * 输入参数的最小个数
	 */
	private int minOccurs = 1;

	/**
	 * 输入参数的最大个数，有两种情况
	 * <ul>
	 *     <li>当传入unbounded时，表示不限制最大输入参数个数</li>
	 *     <li>当传入具体数值时，表示限制具体输入个数</li>
	 * </ul>
	 */
	private String maxOccurs;

}
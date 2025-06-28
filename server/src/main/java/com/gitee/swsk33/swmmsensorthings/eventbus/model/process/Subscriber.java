package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import lombok.Data;

/**
 * API-Processes中表示结果回调的订阅者
 */
@Data
public class Subscriber {

	/**
	 * 任务执行完成时的回调通知地址
	 */
	private String successUri;

	/**
	 * 正在执行/任务状态更新时回调通知地址
	 */
	private String inProgressUri;

	/**
	 * 任务执行失败时回调通知地址
	 */
	private String failedUri;

}
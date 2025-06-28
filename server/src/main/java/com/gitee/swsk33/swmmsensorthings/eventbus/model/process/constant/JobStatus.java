package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant;

/**
 * API-Processes中，任务对象Job的执行状态常量
 */
public class JobStatus {

	/**
	 * 任务已创建
	 */
	public static final String ACCEPTED = "accepted";

	/**
	 * 任务正在运行
	 */
	public static final String RUNNING = "running";

	/**
	 * 任务执行成功
	 */
	public static final String SUCCESSFUL = "successful";

	/**
	 * 任务执行失败
	 */
	public static final String FAILED = "failed";

	/**
	 * 任务被取消
	 */
	public static final String DISMISSED = "dismissed";

}
package com.gitee.swsk33.swmmsensorthings.eventbus.model.process;

import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.AllowedValue;
import com.gitee.swsk33.swmmsensorthings.eventbus.annotation.Required;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.constant.JobStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * API-Processes中，表示一个正在运行的任务实例
 */
@Data
public class Job {

	/**
	 * 任务id
	 */
	@Required
	private String jobID;

	/**
	 * 任务类型，需固定为<code>process</code>
	 */
	@Required
	@AllowedValue("process")
	private String type = "process";

	/**
	 * 任务状态，见{@link JobStatus}中常量
	 */
	@AllowedValue({JobStatus.ACCEPTED, JobStatus.RUNNING, JobStatus.SUCCESSFUL, JobStatus.FAILED, JobStatus.DISMISSED})
	private String status;

	/**
	 * 任务所属的进程{@link Process}对象的ID
	 */
	private String processID;

	/**
	 * 任务消息/描述
	 */
	private String message;

	/**
	 * 任务创建时间
	 */
	private LocalDateTime created = LocalDateTime.now();

	/**
	 * 任务开始执行时间
	 */
	private LocalDateTime started;

	/**
	 * 任务执行完成时间
	 */
	private LocalDateTime finished;

	/**
	 * 任务状态更新时间
	 */
	private LocalDateTime updated;

	/**
	 * 任务执行进度，单位是百分比，取值范围：[0, 100]
	 */
	private int progress;

	/**
	 * 任务相关链接
	 */
	private Link[] links;

}
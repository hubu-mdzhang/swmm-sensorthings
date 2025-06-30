package com.gitee.swsk33.swmmsensorthings.eventbus.model.process.response;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Job;
import com.gitee.swsk33.swmmsensorthings.eventbus.model.process.Link;
import lombok.Data;

import java.util.List;

/**
 * API-Processes的获取全部任务列表响应
 */
@Data
public class JobList {

	/**
	 * 全部任务列表
	 */
	private List<Job> jobs;

	/**
	 * 相关链接对象列表
	 */
	private List<Link> links;

}